package com.agilesolutions.poc.tools;

import com.agilesolutions.poc.dto.DailyShareQuote;
import com.agilesolutions.poc.dto.StockResponse;
import com.agilesolutions.poc.model.DailyStockData;
import com.agilesolutions.poc.model.StockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockTools {


    private final RestTemplate restTemplate;

    @Qualifier("twelveDataClient")
    private final WebClient webClient;

    @Value("${STOCK_API_KEY:demo}")
    private String apiKey;

    /**
     * https://api.twelvedata.com/time_series?symbol=AAPL&interval=1day&outputsize=4&apikey=demo&source=docs
     * https://api.twelvedata.com/time_series?symbol=AAPL&interval=1min&apikey=demo&source=docs
     * @param company
     * @return
     */
    @Tool(description = "get actual stock prices")
    public StockResponse getLatestStockPrices(@ToolParam(description = "Name of company") String company) {
        log.info("Get stock prices for: {}", company);
        StockData data = restTemplate.getForObject("https://api.twelvedata.com/time_series?symbol={0}&interval=1min&outputsize=1&apikey={1}",
                StockData.class,
                company,
                apiKey);
        DailyStockData latestData = data.getValues().get(0);
        log.info("Get stock prices ({}) -> {}", company, latestData.getClose());
        return new StockResponse(Float.parseFloat(latestData.getClose()));
    }

    @Tool(description = "Historical daily stock prices")
    public List<DailyShareQuote> getHistoricalStockPrices(@ToolParam(description = "Search period in days") int days,
                                                          @ToolParam(description = "Name of company") String company) {
        log.info("Get historical stock prices: {} for {} days", company, days);
        StockData data = restTemplate.getForObject("https://api.twelvedata.com/time_series?symbol={0}&interval=1day&outputsize={1}&apikey={2}",
                StockData.class,
                company,
                days,
                apiKey);
        return data.getValues().stream()
                .map(d -> new DailyShareQuote(company, Float.parseFloat(d.getClose()), d.getDatetime()))
                .toList();
    }

}
