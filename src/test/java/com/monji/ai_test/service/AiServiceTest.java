package com.monji.ai_test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;

@SpringBootTest
public class AiServiceTest {

    @Autowired
    private AiService aiService;

    @Test
    public void testGetJoke() {
        var joke = aiService.getJoke("programmers");
        System.out.println(joke);
    }

    @Test
    public void testEmbedText() {
        var embed = aiService.getEmbedding("This is a big text");
        System.out.println(embed.length);
        System.out.println(Arrays.toString(embed));
    }

//    @Test
//    void testConnection() throws Exception {
//        Connection con = DriverManager.getConnection(
//                "jdbc:postgresql://localhost:5434/pgvector-test",
//                "postgres",
//                "root"
//        );
//
//        System.out.println(con.isValid(2));
//    }

    @Test
    void testStoreData() throws Exception {
        aiService.ingestDataToVectorStore();
    }

    @Test
    void testSimilaritySearch() throws Exception {
        var response = aiService.similaritySearch("pirate anime");
        System.out.println(response);
    }

//    @Test
//    void testAskAi() throws Exception {
//        var response = aiService.askAi("what is anime that has an alien boy who strives to be the strongest?");
//        System.out.println(response);
//    }
}
