package com.course1.practicaljava.api.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tire {

    private String manufacturer;

    @JsonProperty(value = "diameter")
    private int size;
    @JsonIgnore
    private int price;


}
