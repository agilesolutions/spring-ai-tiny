package com.agilesolutions.poc.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WhatsUpTools {

    @Tool(description = "Send WhatsUp message")
    public String sendWhatsAppMessage(@ToolParam(description = "Message") String messageBody) {
        // Send a message via Twilio's API

        log.info("WhatsUp message ==>> {}", messageBody);

        return messageBody;

    }

}