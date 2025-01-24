package com.relicary.spring_6_webclient.client;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface BeerClient {

    Flux<String> listBeer();

    Flux<Map<String, String>> listBeerMap();
}
