package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
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
public class ReviewAnalysisController {

    private final ReviewAnalysisService reviewAnalysisService;
    private final PlaceService placeService;

    public ReviewAnalysisController(ReviewAnalysisService reviewAnalysisService, PlaceService placeService) {
        this.reviewAnalysisService = reviewAnalysisService;
        this.placeService = placeService;
    }

    @PostMapping("/analyze-and-save")
    public ResponseEntity<?> analyzeAndSave(@RequestBody RequestModelDto requestDto) {
        try {
            // Place 조회
            PlaceDto placeDto = placeService.getPlaceByName(requestDto.storename());
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // 모델 분석 및 결과 저장
            List<Model2Results> savedResults = reviewAnalysisService.analyzeAndSave(requestDto, place);

            return ResponseEntity.ok(savedResults);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
        }
    }
}