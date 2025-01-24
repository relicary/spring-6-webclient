package com.relicary.spring_6_webclient.client.impl;

import com.relicary.spring_6_webclient.client.BeerClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class BeerClientImplTest {

    @Autowired
    BeerClient client;

    @Test
    void listBeer() {

        Flux<String> beerFlux = client.listBeer();

        StepVerifier
                .create(
                        beerFlux.doOnNext(log::info)
                )
                .expectNextMatches(beer -> beer != null && !beer.isEmpty())
                .verifyComplete();
    }

    @Test
    void testGetMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerMap().subscribe(beer -> {
            log.info(">>> {}",beer);
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }
}