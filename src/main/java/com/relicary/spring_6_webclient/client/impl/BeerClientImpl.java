package com.relicary.spring_6_webclient.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.relicary.spring_6_webclient.client.BeerClient;
import com.relicary.spring_6_webclient.model.BeerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final WebClient webClient;

    public BeerClientImpl(
            WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder.build();
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

    @Override
    public Flux<BeerDTO> listBeersDto() {
        return webClient.get()
                .uri(BEER_PATH)
                .retrieve()
                .bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(BEER_PATH_ID).build(id)
                )
                .retrieve()
                .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getBeerByBeerStyle(String beerStyle) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(BEER_PATH)
                                .queryParam("beerStyle", beerStyle)
                                .build()
                )
                .retrieve()
                .bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
        return webClient.post()
                .uri(BEER_PATH)
                .body(Mono.just(beerDTO), BeerDTO.class)

                .retrieve()

                .toBodilessEntity()
                .flatMap(response ->
                        Mono.just(
                                Objects.requireNonNull(
                                        response.getHeaders().get(HttpHeaders.LOCATION)
                                ).get(0)
                        )
                )
                .map(location -> location.split("/")[location.split("/").length - 1])
                .flatMap(this::getBeerById);
    }
}
