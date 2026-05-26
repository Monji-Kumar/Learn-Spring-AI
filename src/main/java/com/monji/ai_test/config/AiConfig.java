package com.monji.ai_test.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .build();
    }

//    @Bean
//    public EmbeddingModel embeddingModel() {
//       GoogleGenAiApi
//    }
}
