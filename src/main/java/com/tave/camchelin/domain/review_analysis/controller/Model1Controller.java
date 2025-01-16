package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.service.Model1AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model1")
public class Model1Controller {

    private final Model1AnalysisService model1Service;
    private final PlaceService placeService;

    public Model1Controller(Model1AnalysisService model1Service, PlaceService placeService) {
        this.model1Service = model1Service;
        this.placeService = placeService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeReview(@RequestBody Model1RequestDto requestDto) {
        try {
            // Place 조회
            PlaceDto placeDto = placeService.getPlaceByName(requestDto.storename());
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // 모델1 호출 및 결과 저장
            Model1Results result = model1Service.analyzeAndSave(requestDto, place);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
        }
    }
}
