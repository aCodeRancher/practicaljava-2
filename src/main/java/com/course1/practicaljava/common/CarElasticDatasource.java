package com.course1.practicaljava.common;

import com.course1.practicaljava.api.server.entity.Car;
import com.course1.practicaljava.repository.CarElasticRepository;
import com.course1.practicaljava.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Component
public class CarElasticDatasource {

    private static final Logger LOG = LoggerFactory.getLogger(CarElasticDatasource.class);
    @Autowired
    private CarElasticRepository carElasticRepository;

    @Autowired
    @Qualifier("randomCarService")
    private CarService carService;

    @Autowired
    @Qualifier("webClientElasticsearch")
    private WebClient webClient;

    @EventListener(ApplicationReadyEvent.class)
    public void populateData(){
         var response =  webClient.delete().uri("/practical-java")
                                .retrieve().bodyToMono(String.class).block();
         LOG.info("End delete with response {}", response);
         var cars = new ArrayList<Car>();
         for (int i=0;i<10000;i++){
             cars.add(carService.generateCar());
         }

         carElasticRepository.saveAll(cars);
         LOG.info("Saved {} cars", carElasticRepository.count());
         Iterable<Car> carsSaved = carElasticRepository.findAll();
         carsSaved.forEach( car -> LOG.info( "car is id={}, brand is {}", car.getId(), car.getBrand()));
    }

}
