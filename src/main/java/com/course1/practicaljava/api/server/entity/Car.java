package com.course1.practicaljava.api.server.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Car {

    private String brand;
    private String color;
    private String type;

    private int price;
    private boolean available;
    @JsonFormat(pattern="dd-MM-yyyy", timezone = "America/New_York")
    private LocalDate firstRelease;
    @JsonInclude(value= JsonInclude.Include.NON_EMPTY)
    private String secretFeature;
    @JsonInclude(value= JsonInclude.Include.NON_EMPTY)
    private List<String> additionalFeatures;

    @JsonUnwrapped
    private Engine engine;
    private List<Tire> tires;


}
