package com.monji.ai_test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    @Value("classpath:ref.pdf")
    Resource pdfFile;

    public String askAi(String prompt) {

        String template = """
                You are an AI Assistant helping a developer
                
                Rules:
                - Use ONLY the information provided in the context
                - You MAY rephrase, summarize and explain in natural language
                - Do NOT introduce new concepts or facts
                - If multiple context sections are relevant, combine them into a single explanation
                - If the answer i not present, say "I don't know bro"
                
                Context:
                {context}
                
                Answer in a friendly, conversational tone.}
                """;

        List<Document> documents= vectorStore.similaritySearch(SearchRequest.builder()
                .query(prompt)
                .topK(4)
                .similarityThreshold(0.5)
                .filterExpression("file_name == 'ref.pdf'")
                .build());

        String context = documents.stream().map(Document::getText).collect(Collectors.joining("\n\n"));
        PromptTemplate promptTemplate = new PromptTemplate(template);

        String systemPrompt = promptTemplate.render(Map.of("context", context));
        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }

    public void injestPdfToVectorStore() {
        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfFile);

        List<Document> pages = reader.get();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();

        List<Document> chunks = splitter.apply(pages);

        vectorStore.add(chunks);
    }
}
