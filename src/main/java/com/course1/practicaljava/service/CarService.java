package com.course1.practicaljava.service;

import com.course1.practicaljava.api.server.entity.Car;

import java.util.List;

public interface CarService {
    List<String> BRANDS = List.of("Toyota", "Honda", "Ford", "BMW", "Mitsibushi");
    List<String> COLORS = List.of("Red", "Black", "white", "blue", "silver");
    List<String> TYPES = List.of("Sedan", "SUV", "MPV", "Hatchback", "Convertible");
    List<String> ADDITIONAL_FEATURES = List.of("GPS", "Alarm", "Sunroof", "Media player", "Leather seats");
    List<String> FUELS = List.of("Petrol", "Electric", "Hybrid");
    List<String> TIRE_MANUFACTURERS = List.of("Goodyear", "Bridgestone", "Dunlop");

    Car generateCar();

}
