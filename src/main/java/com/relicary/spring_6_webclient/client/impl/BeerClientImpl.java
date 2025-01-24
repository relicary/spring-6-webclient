package com.relicary.spring_6_webclient.client.impl;

import com.relicary.spring_6_webclient.client.BeerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class BeerClientImpl implements BeerClient {

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
                .uri("/api/v3/beer")
                .retrieve()
                .bodyToFlux(String.class);
    }
}
