package com.agilesolutions.poc.rest;

import com.agilesolutions.poc.service.AgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("agent")
public class AgentController {

    private final AgentService agentService;

    @GetMapping("/with-tools")
    public ResponseEntity<String> calculateWalletValueWithTools() {

        ResponseEntity<String> response = ResponseEntity.ok(agentService.calculateWalletValueWithTools());

        return response;
    }

    @GetMapping("/highest-day/{days}")
    public ResponseEntity<String> calculateHighestWalletValue(@PathVariable int days) {

        ResponseEntity<String> response = ResponseEntity.ok(agentService.calculateHighestWalletValue(days));

        return response;

    }
}
