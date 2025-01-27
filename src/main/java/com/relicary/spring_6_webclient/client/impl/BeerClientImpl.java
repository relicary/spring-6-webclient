package com.relicary.spring_6_webclient.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.relicary.spring_6_webclient.client.BeerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/api/v3/beer";
    private final WebClient webClient;

    public BeerClientImpl(
            WebClient.Builder webClientBuilder,
            @Value("${beer.service.url}") String baseUrl) {

        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get()
                .uri(BEER_PATH)
                .retrieve()
                .bodyToFlux(String.class);
    }

    @Override
    public Flux<Map<String, String>> listBeerMap() {
        return webClient.get()
                .uri(BEER_PATH)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, String>>() {});
    }

    @Override
    public Flux<JsonNode> listBeersJsonNode() {
        return webClient.get()
                .uri(BEER_PATH)
                .retrieve()
                .bodyToFlux(JsonNode.class);

    }
}
