package com.relicary.spring_6_webclient.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.relicary.spring_6_webclient.client.BeerClient;
import com.relicary.spring_6_webclient.model.BeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class BeerClientImplTest {

    public static final String BEER_ID_FIELD = "id";
    public static final String BEER_NAME_FIELD = "beerName";

    @Autowired
    BeerClient client;

    @Test
    @Order(1)
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
    @Order(50)
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
    @Order(2)
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
    @Order(51)
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
    @Order(3)
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

    @Order(52)
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

    @Test
    @Order(4)
    void testListBeerDto_withStepVerifier() {
        Flux<BeerDTO> beerFlux = client.listBeersDto();

        StepVerifier
                .create(
                        beerFlux.doOnNext(beer ->
                                log.info(beer.toString())
                        )
                )
                .thenConsumeWhile(beer -> beer != null && beer.getId() != null && beer.getBeerName() != null)
                .verifyComplete();
    }

    @Test
    @Order(53)
    void testListBeerDto_withAwait() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeersDto().subscribe(
                dto -> {
                    log.info(dto.toString());
                    atomicBoolean.set(true);
                }
        );

        await().untilTrue(atomicBoolean);
    }

    @Test
    @Order(5)
    void testGetBeerById_withStepVerifier() {

        Flux<BeerDTO> beerFlux = client.listBeersDto();

        StepVerifier
                .create(beerFlux.flatMap(
                        beerDTO -> client.getBeerById(beerDTO.getId())
                ))
                .expectNextMatches(beer -> beer.getId() != null && beer.getBeerName() != null)
                .thenConsumeWhile(beer -> beer.getId() != null && beer.getBeerName() != null)
                .verifyComplete();


    }

    @Test
    @Order(54)
    void testGetBeerById_withAwait() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        client.listBeersDto()
                .flatMap(beerDto -> client.getBeerById(beerDto.getId()))
                .subscribe(beer -> {
                    log.info(beer.getBeerName());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }
}