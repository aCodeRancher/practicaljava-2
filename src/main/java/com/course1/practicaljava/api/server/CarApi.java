package com.course1.practicaljava.api.server;

import com.course1.practicaljava.api.server.entity.Car;
import com.course1.practicaljava.repository.CarElasticRepository;
import com.course1.practicaljava.service.CarService;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@RequestMapping(value="/api/car/v1")
@RestController
public class CarApi {

    private static final Logger LOG = LoggerFactory.getLogger(CarApi.class);
    @Autowired
    private CarService carService;

    @Autowired
    private CarElasticRepository carElasticRepository;

    @GetMapping(value="/random" , produces = MediaType.APPLICATION_JSON_VALUE)
     public Car random(){
         return carService.generateCar();
     }

     @PostMapping(value="/echo", consumes= MediaType.APPLICATION_JSON_VALUE,
                  produces = MediaType.APPLICATION_JSON_VALUE)
     public Car echo(@RequestBody Car car){
        LOG.info("Car is {}", car);

        return car ;
     }

     @GetMapping(value = "/random-cars", produces=MediaType.APPLICATION_JSON_VALUE)
     public List<Car> randomCars(){
          var result = new ArrayList<Car>();
          for (int i = 0; i< ThreadLocalRandom.current().nextInt(1,10); i++){
                result.add(carService.generateCar());
          }
          return result;
     }

     @GetMapping(value="/count")
     public String countCar(){
        return "There are : "+ carElasticRepository.count() + " cars";
     }

     @PostMapping (value = "", consumes=MediaType.APPLICATION_JSON_VALUE)
     public String saveCar(@RequestBody Car car){
         var id = carElasticRepository.save(car).getId();
         return "Saved with ID: " + id;
     }

     @GetMapping(value = "/{id}")
     public Car getCar(@PathVariable("id") String carId){
        return carElasticRepository.findById(carId).orElse(null);

     }

     @PutMapping(value="/{id}")
     public String updateCar(@PathVariable("id") String carId, @RequestBody Car updatedCar){
        updatedCar.setId(carId);
        var newCar = carElasticRepository.save(updatedCar);
        return "Updated car with ID : " + newCar.getId();
     }

    @GetMapping(value = "/find-json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> findCarsByBrandAndColor(@RequestBody Car car) {
          return carElasticRepository.findByBrandAndColor(car.getBrand(), car.getColor());
    }

    @GetMapping(path={"/cars/{brand}/{color}", "/cars/{brand}"})
    public List<Car> findCarsByPath(@PathVariable String brand,
                                    @PathVariable(name="color", required=false) Optional<String> color){
        if (color.isPresent()) {
            return carElasticRepository.findByBrandAndColor(brand, color);
         }
        else{
             return carElasticRepository.findByBrand(brand);
        }
    }

    @GetMapping(value = "/cars")
    public List<Car> findCarsByParam(@RequestParam String brand, @RequestParam String color){
        return carElasticRepository.findByBrandAndColor(brand, color);
    }

    @GetMapping(value ="/cars/date")
    public List<Car> findCarsReleaseAfter(@RequestParam(name="first_release_date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate firstReleaseDate){

        return carElasticRepository.findByFirstReleaseAfter(firstReleaseDate);

    }
}
