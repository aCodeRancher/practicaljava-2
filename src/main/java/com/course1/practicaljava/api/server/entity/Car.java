package com.course1.practicaljava.api.server.entity;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {

    private String brand;
    private String color;
    private String type;

    private int price;
    private boolean available;
    private LocalDate firstRelease;

    private List<String> additionalFeatures;

}
