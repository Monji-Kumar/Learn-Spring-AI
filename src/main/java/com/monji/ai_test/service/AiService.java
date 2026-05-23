package com.monji.ai_test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

    public String getJoke(String topic) {
        return chatClient.prompt().user("Give me a joke on the topic : " + topic)
                .call().content();
    }
}
