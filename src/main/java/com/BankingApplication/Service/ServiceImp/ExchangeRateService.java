package com.BankingApplication.Service.ServiceImp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Getter
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    private Map<String, Double> rates = new HashMap<>();

    private Set<String> CURRENCIES = Set.of(
            "USD",
            "EUR",
            "GBP",
            "JPY",
            "NGN",
            "INR",
            "VND"
    );

    @Value("${currencyApiKey}") //dùng biến động cho apiKey vì khác với key của jwt luôn cố định, apiKey lấy từ web có thể thường xuyên thay đổi, nên dùng biến động để nếu có thay đổi thì cũng dễ thay đổi apiKey
    private String apiKey;

    public void getExchangeRates()
    {
        String CURRENCY_API = "https://api.currencyapi.com/v3/latest?apikey=";
        var response = restTemplate.getForEntity(CURRENCY_API + apiKey, JsonNode.class);
        var data = Objects.requireNonNull(response.getBody()).get("data"); //lấy body của "data" trong api

        for (var currency : CURRENCIES)
        {
           rates.put(currency, data.get(currency).get("value").doubleValue());
        }

        System.out.println(rates);
    }






}
