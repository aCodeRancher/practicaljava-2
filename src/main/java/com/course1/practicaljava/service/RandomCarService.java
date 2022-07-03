package com.course1.practicaljava.service;

import com.course1.practicaljava.api.server.entity.Car;
import com.course1.practicaljava.util.RandomDateUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomCarService implements CarService{

    @Override
    public Car generateCar(){
          var brand = BRANDS.get(ThreadLocalRandom.current().nextInt(0, BRANDS.size()));
          var color = COLORS.get(ThreadLocalRandom.current().nextInt(0,COLORS.size()));
          var type = TYPES.get(ThreadLocalRandom.current().nextInt(0,TYPES.size()));
          var available = ThreadLocalRandom.current().nextBoolean();
          var price = ThreadLocalRandom.current().nextInt(5000, 12001);
          var firstReleaseDate = RandomDateUtil.generateRandomLocalDate();
          int randomCount = ThreadLocalRandom.current().nextInt(ADDITIONAL_FEATURES.size());
          var additionalFeatures = new ArrayList<String>();
          for (int i=0;i<randomCount;i++){
              additionalFeatures.add(ADDITIONAL_FEATURES.get(i));
          }
        return  new Car(brand , color, type, price, available, firstReleaseDate, additionalFeatures);
    }
}
