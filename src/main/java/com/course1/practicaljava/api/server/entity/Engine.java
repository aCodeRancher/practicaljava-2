package com.course1.practicaljava.api.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value={"color", "serialNumber"})
public class Engine {

   private String fuelType;
   private int horsePower;


   private String color="Black";


   private String serialNumber = "SN00001";

}
