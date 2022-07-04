package com.course1.practicaljava.repository;

import com.course1.practicaljava.api.server.entity.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarElasticRepository extends ElasticsearchRepository<Car, String> {

 List<Car> findByBrandAndColor(String brand, String color );



}
