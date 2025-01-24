package com.relicary.spring_6_webclient.client.impl;

import com.relicary.spring_6_webclient.client.BeerClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
class BeerClientImplTest {

    @Autowired
    BeerClient client;

    @Test
    void listBeer() {

        Flux<String> beerFlux = client.listBeer();

        StepVerifier.create(beerFlux.doOnNext(log::info))
                .expectNextMatches(beer -> beer != null && !beer.isEmpty())
                .verifyComplete();
    }

}