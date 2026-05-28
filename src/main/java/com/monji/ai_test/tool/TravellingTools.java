package com.monji.ai_test.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TravellingTools {

    @Tool(description = "Get the weather of a city")
    public String getWeather(@ToolParam(description = "City name for whom to get the weather information about", required = true) String city) {
        switch (city) {
            case "Delhi" -> {
                return "Sunny, 26 Degrees Celcius";
            }
            case "London" -> {
                return "Windy, 16 Degrees Celcius";
            }
            case "Paris" -> {
                return "Rainy, 20 Degrees Celcius";
            }
            default -> {
                return "Unknown";
            }
        }
    }
}
