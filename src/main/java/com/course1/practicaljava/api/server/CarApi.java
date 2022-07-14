package com.course1.practicaljava.api.server;

import com.course1.practicaljava.api.response.ErrorResponse;
import com.course1.practicaljava.api.server.entity.Car;
import com.course1.practicaljava.exception.IllegalApiParamException;
import com.course1.practicaljava.repository.CarElasticRepository;
import com.course1.practicaljava.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@RequestMapping(value="/api/car/v1")
@RestController
@Tag(name="Car API", description="Documentation for Car API")
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

     @Operation(summary="Echo car", description="Echo given car input")
     @PostMapping(value="/echo", consumes= MediaType.APPLICATION_JSON_VALUE,
                  produces = MediaType.APPLICATION_JSON_VALUE)
     public Car echo(@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "car to be echoed")
                          @RequestBody Car car){
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

        Optional<Car> carFound = carElasticRepository.findById(carId);
        if (carFound.isPresent()) {
            updatedCar.setId(carId);
            carElasticRepository.save(updatedCar);
        }
        return "Updated car with ID : " + updatedCar.getId();
     }

    @GetMapping(value = "/find-json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> findCarsByBrandAndColor(@RequestBody Car car) {
          return carElasticRepository.findByBrandAndColor(car.getBrand(), car.getColor());
    }

    @GetMapping(path={"/cars/{brand}/{color}", "/cars/{brand}"})
    @Operation(summary = "find cars by path", description="Find cars by path variable")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Everything is ok"),
                     @ApiResponse(responseCode = "400", description = "Bad input parameter")
                 })
    public ResponseEntity<Object> findCarsByPath(
            @Parameter(description = "Brand to be find") @PathVariable String brand,
            @Parameter(description = "Color to be find") @PathVariable(name="color", required=false) Optional<String> color,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue="0")int page,
            @Parameter(description= "Number of items per page for pagination")   @RequestParam(defaultValue="50")int size){

        var pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "price"));
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SERVER, "Spring");
        headers.add("X-Custom-Header", "Custom Response Header");

        if (color.isPresent()) {
             String colorString = color.get();
             if (StringUtils.isNumeric(colorString)){
                var errorResponse = new ErrorResponse("Invalid color : "+ color, LocalDateTime.now());
               // return new ResponseEntity<Object>(errorResponse, headers, HttpStatus.BAD_REQUEST);
               return ResponseEntity.badRequest()
                       .header("X-Custom-Header", "Custom Response Header")
                       .header(HttpHeaders.SERVER, "Spring")
                       .body(errorResponse);
             }
            var cars = carElasticRepository.findByBrandAndColor(brand, color, pageable).getContent();
            return ResponseEntity.ok().headers(headers).body(cars);
         }
        else{
             var cars = carElasticRepository.findByBrand(brand);
             return ResponseEntity.ok().headers(headers).body(cars);
        }
    }

    @GetMapping(value = "/cars")
    public List<Car> findCarsByParam(@RequestParam String brand, @RequestParam String color,
                                     @RequestParam(defaultValue="0") int page,
                                     @RequestParam(defaultValue="10") int size){
        if (StringUtils.isNumeric(color)){
            throw new IllegalApiParamException("Invalid color : "+ color);
        }
        if (StringUtils.isNumeric(brand)){
            throw new IllegalApiParamException(("Invalid brand : "+ brand));
        }
        var pageable = PageRequest.of(page, size);
        Optional<String> colorOptional = Optional.of(color);
        return carElasticRepository.findByBrandAndColor(brand, colorOptional, pageable).getContent();
    }

    @GetMapping(value ="/cars/date")
    public List<Car> findCarsReleaseAfter(@RequestParam(name="first_release_date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate firstReleaseDate){

        return carElasticRepository.findByFirstReleaseAfter(firstReleaseDate);

    }
}
