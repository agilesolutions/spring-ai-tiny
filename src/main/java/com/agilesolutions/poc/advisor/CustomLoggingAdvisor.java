package com.agilesolutions.poc.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;

@Slf4j
public class CustomLoggingAdvisor implements CallAroundAdvisor {

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

        advisedRequest = this.before(advisedRequest);

        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

        this.observeAfter(advisedResponse);

        return advisedResponse;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("OBSERVE AFTER => {}",advisedResponse.response()
                .getResult()
                .getOutput()
                .toString());

    }

    private AdvisedRequest before(AdvisedRequest advisedRequest) {
        log.info("OBSERVE BEFORE => {}",advisedRequest.userText());
        return advisedRequest;
    }

    @Override
    public String getName() {
        return "CustomLoggingAdvisor";
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}