package com.course1.practicaljava.api.server;

import com.course1.practicaljava.api.server.entity.CarPromotion;
import com.course1.practicaljava.exception.IllegalApiParamException;
import com.course1.practicaljava.repository.CarPromotionElasticRepository;
import com.course1.practicaljava.service.CarPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/car/v1")
public class CarPromotionApi {

    @Autowired
    private CarPromotionService carPromotionService;

    @Autowired
    private CarPromotionElasticRepository carPromotionElasticRepository;

    @GetMapping(value="/promotions")
    public List<CarPromotion> listAvailablePromotion(@RequestParam(name="type") String promotionType,
                                                    @RequestParam(defaultValue="0") int page,
                                                     @RequestParam(defaultValue="10") int size) {
        if (!carPromotionService.isValidPromotionType(promotionType)){
            throw new IllegalApiParamException("Invalid promotion type : " + promotionType);

        }
        var pageable = PageRequest.of(page,size);
        return carPromotionElasticRepository.findByType(promotionType, pageable).getContent();

    }
}
