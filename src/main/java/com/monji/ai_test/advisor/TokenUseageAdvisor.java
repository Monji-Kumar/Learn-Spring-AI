package com.monji.ai_test.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;

@Slf4j
public class TokenUseageAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        Long startTime = System.currentTimeMillis();

        //1. Pass the request down the chain
        ChatClientResponse advicedResponse = callAdvisorChain.nextCall(chatClientRequest);

        //2. Extract the actual LLM response
        ChatResponse chatResponse = advicedResponse.chatResponse();

        //3. Inspect Usage Metadata
        if(chatResponse != null && chatResponse.getMetadata().getUsage() != null){
            var usage = chatResponse.getMetadata().getUsage();
            long duration = System.currentTimeMillis() - startTime;

            log.info("💰 Token Usage : Input = {} | Output = {} | Total = {} | Time = {}ms",
                    usage.getPromptTokens(),
                    usage.getCompletionTokens(),
                    usage.getTotalTokens(),
                    duration);

            //Possible Usage is to store and track Token Usage per User
        }

        return advicedResponse;
    }

    @Override
    public String getName() {
        return "ChatClientResponse";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
