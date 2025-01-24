package com.relicary.spring_6_webclient.client;

import reactor.core.publisher.Flux;

public interface BeerClient {

    Flux<String> listBeer();
}
