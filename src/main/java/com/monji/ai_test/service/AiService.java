package com.monji.ai_test.service;

import com.google.gson.JsonObject;
import com.monji.ai_test.dto.JokeDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

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
