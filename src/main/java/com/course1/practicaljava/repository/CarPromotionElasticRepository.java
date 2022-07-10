package com.course1.practicaljava.repository;

import com.course1.practicaljava.api.server.entity.CarPromotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPromotionElasticRepository extends ElasticsearchRepository<CarPromotion, String> {

    Page<CarPromotion> findByType(String type, Pageable pages);

}
