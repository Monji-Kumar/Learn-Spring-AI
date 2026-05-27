package com.monji.ai_test.service;

import com.google.gson.JsonObject;
import com.monji.ai_test.dto.JokeDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }

    public void ingestDataToVectorStore() {
        List<Document> anime = List.of(
                new Document("A young boy whose family was murdered by demon-king and his sister turned into demon" +
                        " went on the jouney to take revenge and cure his sister",
                        Map.of("title", "Demon Slayer", "Japanese-Title", "Kimetsu-no-yaiba",
                                "year","2016", "genre", "adventure")),
                new Document("A young boy sails off to sea to achieve his dream of becoming the king of the pirates",
                        Map.of("title", "One Piece", "Japanese-Title", "One Piece",
                                "year","1999", "genre", "world-building")),
                new Document("A young boy is introduced to The Honoured ONE - GOJO Satoru",
                        Map.of("title", "Jujutsu Kaisen", "Japanese-Title", "Jujutsu Kaisen",
                                "year","2016", "genre", "Magic"))

        );

        vectorStore.add(anime);
        vectorStore.add(springAIDocs());
    }

    public static List<Document> springAIDocs() {
        return List.of(
                new Document("A young alien boy who strives to be the strongest just to protect his loved ones",
                        Map.of("title", "Dragon Ball Series", "Japanese-Title", "Dragon Ball Series", "year", "1999", "genre", "Adventure")),
                new Document("A young boy who discoves a parallel soul world of grim reapers and becomes their favourite",
                        Map.of("title", "Bleach Series", "Japanese-Title", "Bleach Series", "year", "2001", "genre", "Magic")),
                new Document("A young boy discovers a diary that can murder anyone just by knowing their face and name",
                        Map.of("title", "Death Note", "Japanese-Title", "Death Note", "year", "2003", "genre", "Thriller")),
                new Document("A young man trains so hard that he loses his hair but becomes so strong he can defeat anyone with one puch",
                        Map.of("title", "One Punch Man", "Japanese-Title", "One Punch Man", "year", "2015", "genre", "Adventure"))
        );
    }

    public List<Document> similaritySearch(String text) {
       return vectorStore.similaritySearch(SearchRequest.builder()
                       .query(text)
                       .topK(2)
                       .similarityThreshold(0.6)
               .build());
    }

    public JSONObject getJoke(String topic) {

        String systemPrompt = """
            You are a sarcastic joker, give response in 4 line only.
            Give me a joke on the topic : {topic}
        """;

        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String renderedText = promptTemplate.render(Map.of("topic", topic));
        JSONObject jsonObject = new JSONObject();
        var response = chatClient.prompt().user(renderedText)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call();
        jsonObject.put("text", renderedText);
        jsonObject.put("entityText", response.entity(JokeDto.class).text());
//        jsonObject.put("content", response.content());
//        jsonObject.put("response", response.chatClientResponse());

        return jsonObject;

    }
}
