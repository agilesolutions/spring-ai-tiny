package com.agilesolutions.poc.rest;

import com.agilesolutions.poc.service.AgentService;
import com.agilesolutions.poc.tools.DateTimeTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("date")
public class DateTimeController {

    private final ChatModel chatModel;

    private final DateTimeTools dateTimeTools;

    @GetMapping("/setAlarm")
    public ResponseEntity<String> getSharesInWallet() {

        String response = ChatClient.create(chatModel)
                .prompt("Can you set an alarm 10 minutes from now?")
                .tools(dateTimeTools)
                .call()
                .content();

        log.info(response);

        return ResponseEntity.ok(response);

    }
}