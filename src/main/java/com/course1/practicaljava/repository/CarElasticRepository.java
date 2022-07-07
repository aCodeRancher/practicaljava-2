package com.course1.practicaljava.repository;

import com.course1.practicaljava.api.server.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarElasticRepository extends ElasticsearchRepository<Car, String> {

 Page<Car> findByBrandAndColor(String brand, Optional<String> color, Pageable pageable );
 List<Car> findByBrand(String brand);
 List<Car> findByBrandAndColor(String brand, String color);

 List<Car> findByFirstReleaseAfter(LocalDate firstRelease);
}
