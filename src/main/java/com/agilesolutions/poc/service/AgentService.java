package com.agilesolutions.poc.service;

import com.agilesolutions.poc.advisor.CustomLoggingAdvisor;
import com.agilesolutions.poc.tools.StockTools;
import com.agilesolutions.poc.tools.WalletTools;
import com.agilesolutions.poc.tools.WhatsUpTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final ChatModel chatModel;

    private final ChatClient chatClient;

    private final StockTools stockTools;

    private final WalletTools walletTools;

    private final WhatsUpTools whatsUpTools;


    public String getSharesInWallet() {
        String message = """
        fetch company name and quantity for all shares in my wallet send all resulting details you fetched in a message to WhatsUp  
        """;


        String systemMessage = """
  You are a helpful assistant who answers questions about my wallet. 
  Use your training data to provide answers about the questions. 
  If the requested information is not available in your training data, use the provided Tools to get the information.
  The tool response is in JSON format. Use the provided WhatsUp tool to ONLY! send that JSON formatted data as a message when you sending a WhatsUp message, ONLY IN CASE YOU GATHERED DATA FROM MY WALLET. 
  If the requested information is not available from any sources, then respond by explaining the reason that the information is not available. 

""";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessage);
        ChatResponse chatResponse = ChatClient.builder(chatModel).build()
                .prompt(systemPromptTemplate.create())
                .tools(walletTools, whatsUpTools) // Provide the tool reference to the LLM
                .user(message)
                .advisors(new SimpleLoggerAdvisor(), new CustomLoggingAdvisor())
                .call()
                .chatResponse();

        return "Response: " + chatResponse.getResult().getOutput().getText();

    }

    public String calculateWalletValue() {
        //What’s the current value in dollars of my wallet based on the latest stock daily prices ?
        String message = """
        What’s the current value in dollars of my wallet based on the latest stock daily prices ?
        """;


        String systemMessage = """
  You are a helpful assistant who answers questions about actual stock prices for shares. 
  Use your training data to provide answers about the questions. 
  If the requested information is not available in your training data, use the provided Tools to get the information.
  Use the company name from share to get the actual stock price for shares. 
  If the requested information is not available from any sources, then respond by explaining the reason that the information is not available. 

""";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessage);
        ChatResponse chatResponse = ChatClient.builder(chatModel).build()
                .prompt(systemPromptTemplate.create())
                .tools(walletTools, stockTools, whatsUpTools) // Provide the tool reference to the LLM
                .user(message)
                .advisors(new SimpleLoggerAdvisor(), new CustomLoggingAdvisor())
                .call()
                .chatResponse();

        return "Response: " + chatResponse.getResult().getOutput().getText();

    }

    public String calculateHighestWalletValue(@PathVariable int days) {
        PromptTemplate pt = new PromptTemplate("""
        On which day during last {days} days my wallet had the highest value in dollars based on the historical daily stock prices ?
        """);

        return this.chatClient.prompt(pt.create(Map.of("days", days)))
                .tools(stockTools, walletTools)
                .call()
                .content();
    }
}
