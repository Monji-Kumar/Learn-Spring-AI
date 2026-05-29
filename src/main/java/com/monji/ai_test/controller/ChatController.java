package com.monji.ai_test.controller;

import com.monji.ai_test.tool.FlightBookingTools;
import com.monji.ai_test.tool.TravellingTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;
    private final TravellingTools travellingTools;
    private final FlightBookingTools flightBookingTools;
    private final ChatMemory chatMemory;

    @PostMapping("/chat")
    public String chat(@RequestBody String message, @RequestParam String userId) {

        String systemPrompt = String.format("""
                You are a friendly Flight booking assistant!
                Use the available tools to create, view and update bookings.
                ALWAYS confirm actions with the user when possible.
                
                IMPORTANT: The current user's ID is "%s".
                When calling tools that require a userId. ALWAYS use this exact value
                """, userId);

        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .tools(travellingTools, flightBookingTools)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }

}
