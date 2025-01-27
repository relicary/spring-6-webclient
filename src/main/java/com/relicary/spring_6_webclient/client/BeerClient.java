package com.relicary.spring_6_webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface BeerClient {

    Flux<String> listBeer();

    Flux<Map<String, String>> listBeerMap();

    Flux<JsonNode> listBeersJsonNode();
}
