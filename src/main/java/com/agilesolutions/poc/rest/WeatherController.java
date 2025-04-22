package com.agilesolutions.poc.rest;

import com.agilesolutions.poc.tools.WeatherTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("weather")
public class WeatherController {

    private final ChatModel chatModel;

    private final WeatherTool weatherTool;

    /**
     * user prompt ... What is the forecast for Amsterdam for tomorrow?
     * @param message
     * @return
     */
    @GetMapping("/getWeather")
    public String chatWithTool(@RequestParam("message") String message) {
        String systemMessage = """
  You are a helpful assistant who answers questions about Weather. 
  Use your training data to provide answers about the questions. 
  If the requested information is not available in your training data, use the provided Tools to get the information.
  The tool response is in JSON format. The forecasts for different time periods are available under the field named "periods". 
  If the requested information is not available from any sources, then respond by explaining the reason that the information is not available. 

""";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessage);
        ChatResponse chatResponse = ChatClient.builder(chatModel).build()
                .prompt(systemPromptTemplate.create())
                .tools(weatherTool) // Provide the tool reference to the LLM
                .user(message)
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .chatResponse();

        return "Response: " + chatResponse.getResult().getOutput().getText();
    }

}