package com.course1.practicaljava.api.server;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        webTestClient.get().uri("/api/time").exchange()
                .expectBody(String.class).value(v-> isCorrectTime(v));
    }

    private Object isCorrectTime(String v){
        var responseLocalTime = LocalTime.parse(v);
        var now = LocalTime.now();
        var nowMinus30Seconds = now.minusSeconds(30);
        assertTrue(responseLocalTime.isAfter(nowMinus30Seconds)
                && responseLocalTime.isBefore(now));
      return responseLocalTime;
    }

    @Test
    void headerByAnnotation() {
        var headerOne = "Spring Boot Test";
        var headerTwo = "Spring Boot Test on Practical Java";
        webTestClient.get().uri("/api/header-one")
                .header("User-agent", headerOne)
                .header("Practical-Java", headerTwo)
                .exchange().expectBody(String.class)
                .value( v -> {
                             assertTrue(v.contains(headerOne));
                              assertTrue(v.contains(headerTwo));
                            }
                  );
     }

    @Test
    void headerByRequest() {
         var header1 = "Spring Boot Test";
         var header2 = "Spring Boot Test on Practical Java";
        webTestClient.get().uri("/api/header-two")
                .header("User-agent", header1)
                .header("Practical-Java", header2)
                .exchange().expectBody(String.class)
                .value( v-> {
                         assertTrue(v.contains(header1));
                         assertTrue(v.contains(header2));
                });
    }
}