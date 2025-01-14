package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.service.Model2IntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/model2")
public class Model2Controller {

    private final Model2IntegrationService model2IntegrationService;
    private final PlaceService placeService;

    public Model2Controller(Model2IntegrationService model2IntegrationService, PlaceService placeService) {
        this.model2IntegrationService = model2IntegrationService;
        this.placeService = placeService;
    }

    @PostMapping("/analyze-and-save")
    public ResponseEntity<?> analyzeAndSave(@RequestBody Model2RequestDto requestDto) {
        try {
            // Place 조회
            PlaceDto placeDto = placeService.getPlaceByName(requestDto.storename());
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // 모델 호출 및 결과 저장
            List<ReviewAnalysis> savedReviews = model2IntegrationService.analyzeAndSave(requestDto, place);

            return ResponseEntity.ok(savedReviews);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
        }
    }

}
