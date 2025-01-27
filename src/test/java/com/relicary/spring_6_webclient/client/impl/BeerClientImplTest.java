package com.relicary.spring_6_webclient.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.relicary.spring_6_webclient.client.BeerClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class BeerClientImplTest {

    public static final String BEER_ID_FIELD = "id";
    public static final String BEER_NAME_FIELD = "beerName";

    @Autowired
    BeerClient client;

    @Test
    void testListBeerString_withStepVerifier() {

        Flux<String> listBeer = client.listBeer();

        StepVerifier
                .create(
                        listBeer.doOnNext(log::info)
                )
                .expectNextMatches(beer -> beer != null && !beer.isEmpty())
                .verifyComplete();
    }

    @Test
    void testListBeerString_withAwait() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeer()
                .subscribe(response -> {
                    log.info(response);
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);

    }

    @Test
    void testListBeerMap_withStepVerifier() {
        Flux<Map<String, String>> listBeerMap = client.listBeerMap();

        StepVerifier
                .create(
                        listBeerMap.doOnNext(
                                beer ->
                                        log.info(beer.toString())
                        )
                )
                .thenConsumeWhile(beer -> beer != null && beer.containsKey(BEER_ID_FIELD) && beer.containsKey(BEER_NAME_FIELD))
                .verifyComplete();
    }

    @Test
    void testListBeerMap_withAwait() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeerMap()
                .subscribe(beer -> {
                    log.info(beer.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testListBeerJson_withStepVerifier() {
        Flux<JsonNode> beerFlux = client.listBeersJsonNode();

        StepVerifier
                .create(
                        beerFlux.doOnNext(beer ->
                                log.info(beer.toPrettyString())
                        )
                )
                .thenConsumeWhile(beer -> beer != null && beer.has(BEER_ID_FIELD) && beer.has(BEER_NAME_FIELD))
                .verifyComplete();
    }

    @Test
    void testListBeerJson_withAwait() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeersJsonNode()
                .subscribe(jsonNode -> {
                    log.info(jsonNode.toPrettyString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }
}