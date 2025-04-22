package com.agilesolutions.poc.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class WeatherTool {

    private RestClient restClient;

    @Tool(description = "Get the weather for a given location")
    public String getWeather(String location) {
        // location is only used for logging. The coordinates are hardcoded in the URL
        log.info("getWeather tool was invoked with location: {}", location);
        restClient = RestClient.builder().build();
        // URL to get the forecast for BOSTON, MA

        String response = restClient.get().uri("https://api.weather.gov/gridpoints/BOX/71,90/forecast")
                .retrieve().body(String.class);
        log.info("Response from the weather API: {}", response);
        return response;
    }
}