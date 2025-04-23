package com.agilesolutions.poc.tools;


import com.agilesolutions.poc.config.RestConfig;
import com.agilesolutions.poc.dto.DailyShareQuote;
import com.agilesolutions.poc.dto.StockResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(classes = {RestConfig.class, StockTools.class}, initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
class StockToolsTest {

    @Autowired
    StockTools stockTools;

    /**
     * https://api.twelvedata.com/time_series?symbol=AAPL&interval=1day&outputsize=4&apikey=demo&source=docs
     */

    @Test
    public void givenAvailable_whenRetrieving_thenReturnStocks() throws JsonProcessingException {

        StockResponse response = stockTools.getLatestStockPrices("AAPL");

        assertAll("verify result",
                () -> assertTrue(response.price() > 0)
        );


    }

    @Test
    public void givenAvailable_whenRetrieving_thenReturnStockss() throws JsonProcessingException {

        List<DailyShareQuote>  dailyShareQuotes = stockTools.getHistoricalStockPrices(5,"AAPL");

        assertAll("verify result",
                () -> assertTrue(dailyShareQuotes.size() > 0)
        );


    }


}