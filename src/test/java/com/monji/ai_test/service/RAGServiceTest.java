package com.monji.ai_test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RAGServiceTest {
    @Autowired
    RAGService ragService;

    @Test
    public void testInjestVectorStore() throws Exception {
        ragService.injestPdfToVectorStore();
    }

    @Test
    public void testAskAi(){
        var response = ragService.askAi("Tell me about Evanglion");
        System.out.println(response);
    }

    @Test
    public void testAskAiWithAdvisors(){
        var response = ragService.askAIWithAdvisors("Suggest a new Action anime to me", "monji00");
        System.out.println(response);
    }
}
