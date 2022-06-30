package com.course1.practicaljava.service;

import com.course1.practicaljava.api.server.entity.Car;

import java.util.List;

public interface CarService {
    List<String> BRANDS = List.of("Toyota", "Honda", "Ford");
    List<String> COLORS = List.of("Red", "Black", "white");
    List<String> TYPES = List.of("Sedan", "SUV", "MPV");

    Car generateCar();

}
