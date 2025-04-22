package com.agilesolutions.poc.tools;

import com.agilesolutions.poc.model.Share;
import com.agilesolutions.poc.repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletTools {

    private final WalletRepository walletRepository;

    private final ObjectMapper objectMapper;

    @Tool(description = "fetch company name and quantity for all shares in my wallet", returnDirect = false)
    public String getAllShares() {
        String jsonShares = null;

        List<Share>  shares =  (List<Share>) walletRepository.findAll();

        log.info("all share {}", shares.stream().map(s -> s.getCompany()).collect(Collectors.joining(";")));

        try {
            jsonShares =  objectMapper.writeValueAsString(shares);
            log.info("Shares converted to JSON {}", jsonShares);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonShares;

    }

}