package com.course1.practicaljava.api.server;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefaultRestApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void welcome() {
        webTestClient.get().uri("/api/welcome")
                .exchange().expectStatus().is2xxSuccessful().expectBody(String.class)
                .value(IsEqualIgnoringCase.equalToIgnoringCase("welcome to spring boot"));

    }

    @Test
    void time() {
    }

    @Test
    void headerByAnnotation() {
    }

    @Test
    void headerByRequest() {
    }
}