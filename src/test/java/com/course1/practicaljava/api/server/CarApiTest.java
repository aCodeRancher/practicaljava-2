package com.course1.practicaljava.api.server;

import com.course1.practicaljava.api.server.entity.Car;
import com.course1.practicaljava.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    @Qualifier("randomCarService")
    private CarService carService;

    @Test
    void random() {
        for (int i=0;i<100;i++) {
            webTestClient.get().uri("/api/car/v1/random")
                    .exchange().expectBody(Car.class).value(car ->{
                        assertTrue(CarService.BRANDS.contains(car.getBrand()));
                        assertTrue(CarService.COLORS.contains(car.getColor()));
                     });
        }
    }

    @Test
    void randomCars() {
        webTestClient.get().uri("/api/car/v1/random-cars")
                .exchange().expectBodyList(Car.class)
                .value( cars -> assertTrue(cars.size()>0));
    }

    @Test
    void countCar() {
         webTestClient.get().uri("/api/car/v1/count")
                .exchange().expectBody(String.class).value (s ->
                 {
                     assertTrue(s.contains("There are"));
                 });
     }

    @Test
    void saveCar() {
        var randomCar = carService.generateCar();
       for (int i=0;i<100;i++) {
           assertTimeout(Duration.ofSeconds(1), () ->
                   webTestClient.post().uri("/api/car/v1")
                           .contentType(MediaType.APPLICATION_JSON)
                           .bodyValue(randomCar)
                           .exchange().expectStatus().is2xxSuccessful());
       }

    }

    @Test
    void getCar() {
        String dateofRelease  = "2010-01-01";
        AtomicReference<Car> firstCar = new AtomicReference<>();
        //get the first car from the output
        webTestClient.get().uri(uriBuilder-> uriBuilder.path("/api/car/v1/cars/date")
                        .queryParam("first_release_date", dateofRelease).build())
                        .exchange().expectBodyList(Car.class).value(
                        cars -> {
                            firstCar.set(cars.get(0));
                        }
                );
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/car/v1/{id}")
                        .build(firstCar.get().getId()))
                .exchange().expectBody(Car.class).value(
                        car -> assertTrue( car.getId().equals(firstCar.get().getId()))
                );
    }

    @Test
    void updateCar() {
        String dateofRelease  = "2010-01-01";
        AtomicReference<Car> firstCar = new AtomicReference<>();
       //get the first car from the output
        webTestClient.get().uri(uriBuilder-> uriBuilder.path("/api/car/v1/cars/date")
                        .queryParam("first_release_date", dateofRelease).build())
                .exchange().expectBodyList(Car.class).value(
                        cars -> {
                            firstCar.set(cars.get(0));
                        }
                );
        //update the first car's color to cyan
         Car updatedCar = firstCar.get();
         final String newColor = "cyan";
         updatedCar.setColor(newColor);
        webTestClient.put().uri(uriBuilder -> uriBuilder.path("/api/car/v1/{id}")
                        .build(firstCar.get().getId()))
                        .bodyValue(updatedCar).exchange().expectStatus().is2xxSuccessful();
        //get the updated car color
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/car/v1/{id}")
                        .build(firstCar.get().getId()))
                         .exchange().expectBody(Car.class).value(
                                  car -> assertTrue( car.getColor().equals(newColor))
                );
    }

    @Test
    void findCarsByBrandAndColor() {
        Car car = new Car();
        final String brand = "Toyota";
        final String color = "Red";
        car.setBrand(brand);
        car.setColor(color);
        webTestClient.method(HttpMethod.GET).uri("/api/car/v1/find-json")
                .bodyValue(car).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Car.class).value(cars ->
                  {
                    cars.forEach(c -> {
                        assertTrue(c.getBrand().equals(brand));
                        assertTrue(c.getColor().equals(color));
                    });

                });
    }

    @Test
    void findCarsByPath() {
        final int size = 5;
        for (var brand: CarService.BRANDS){
            for (var color: CarService.COLORS) {
                AtomicInteger counter= new AtomicInteger();
                webTestClient.get().uri(uriBuilder ->
                                uriBuilder.path("/api/car/v1/cars/{brand}/{color}")
                                        .queryParam("page", 0)
                                        .queryParam("size", size).build(brand, color))
                        .exchange().expectStatus().is2xxSuccessful()
                        .returnResult(Object.class).getResponseBody()
                              .subscribe(
                                object -> {
                                    counter.getAndIncrement();
                                    assertTrue(((Car) object).getBrand().equals(brand));
                                    assertTrue(((Car) object).getColor().equals(color));
                                });
                assertTrue(counter.get()<=size);
            }
        }
    }

    @Test
    void findCarsByParam() {
        final int size = 5;
        for (var brand: CarService.BRANDS){
            for (var color: CarService.COLORS){
                 webTestClient.get().uri(uriBuilder->
                         uriBuilder.path("/api/car/v1/cars").queryParam("brand", brand)
                                 .queryParam("color", color).queryParam("page",0)
                                 .queryParam("size",size).build())
                                 .exchange().expectBodyList(Car.class).value(
                                         cars -> {
                                             assertNotNull(cars);
                                             assertTrue(cars.size()<=size);
                                         }
                         );
            }
        }
    }

    @Test
    void findCarsReleaseAfter() {
        String dateofRelease  = "2010-01-01";
        webTestClient.get().uri(uriBuilder-> uriBuilder.path("/api/car/v1/cars/date")
                    .queryParam("first_release_date", dateofRelease).build())
                    .exchange().expectBodyList(Car.class).value(
                        cars -> {
                            assertNotNull(cars);
                            assertTrue(cars.size()>0);
                         }
                 );
    }
}