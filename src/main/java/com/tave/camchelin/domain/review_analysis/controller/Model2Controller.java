package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.review_analysis.service.Model1AnalysisService;
import com.tave.camchelin.domain.review_analysis.service.Model2AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/model2")
public class Model2Controller {

    private final Model2AnalysisService model2AnalysisService;
    private final Model1AnalysisService model1AnalysisService;
    private final PlaceService placeService;

    public Model2Controller(Model2AnalysisService model2AnalysisService, Model1AnalysisService model1AnalysisService, PlaceService placeService) {
        this.model2AnalysisService = model2AnalysisService;
        this.model1AnalysisService = model1AnalysisService;
        this.placeService = placeService;
    }

    @PostMapping("/analyze-and-save")
    public ResponseEntity<?> analyzeAndSave(@RequestBody Model2RequestDto requestDto) {
        try {
            // Place 조회
            PlaceDto placeDto = placeService.getPlaceByName(requestDto.storename());
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // 모델1 결과 조회
            Model1Results model1Result = model1AnalysisService.findByStoreName(requestDto.storename());
            if (model1Result == null) {
                return ResponseEntity.badRequest().body("Model1 results not found for the store");
            }

            // 모델 호출 및 결과 저장
            List<Model2Results> savedReviews = model2AnalysisService.analyzeAndSave(requestDto, place, model1Result);

            return ResponseEntity.ok(savedReviews);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
        }
    }

}
