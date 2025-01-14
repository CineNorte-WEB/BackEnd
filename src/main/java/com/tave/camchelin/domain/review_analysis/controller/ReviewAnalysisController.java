package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.service.ModelIntegrationService;
import com.tave.camchelin.domain.review_analysis.service.ReviewAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/review-analysis")
public class ReviewAnalysisController { //db에 저장

    //private final ReviewAnalysisService reviewAnalysisService;
    private final ModelIntegrationService modelIntegrationService;
    private final PlaceService placeService;

    public ReviewAnalysisController(ModelIntegrationService modelIntegrationService, PlaceService placeService) {
        this.modelIntegrationService = modelIntegrationService;
        this.placeService = placeService;
    }

    /**
     * 모델 분석 결과 호출 및 저장
     */
//    @PostMapping("/analyze-and-save")
//    public ResponseEntity<?> analyzeAndSave(@RequestBody RequestModelDto requestModelDto) {
//        try {
//            // Place 엔티티 조회
//            PlaceDto placeDto = placeService.getPlaceByName(requestModelDto.storename());
//            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));
//
//            // 모델 호출 및 결과 저장
//            ReviewAnalysis savedReview = reviewAnalysisService.analyzeAndSave(requestModelDto, place);
//
//            return ResponseEntity.ok(savedReview);
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
//        }
//    }

    @PostMapping("/process-and-save")
    public ResponseEntity<?> processAndSave(@RequestBody RequestModelDto requestModelDto) {
        try {
            // Place 정보 조회
            PlaceDto placeDto = placeService.getPlaceByName(requestModelDto.storename());
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // 모델1과 모델2 호출 및 결과 저장
            List<ReviewAnalysis> savedResults = modelIntegrationService.processAndSave(requestModelDto, place);

            return ResponseEntity.ok(savedResults);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}