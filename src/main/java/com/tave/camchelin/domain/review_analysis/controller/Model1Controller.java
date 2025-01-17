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

    @PostMapping("/generate")
    public ResponseEntity<?> analyzeReview(@RequestBody Model1RequestDto requestDto) {
        try {
            System.out.println("📌 모델1 분석 요청 도착: " + requestDto.storeName());

            // ✅ 가게 정보 조회 (DB에 존재하는지 확인)
            PlaceDto placeDto = placeService.getPlaceByName(requestDto.storeName());
            if (placeDto == null) {
                System.out.println("❌ 해당 스토어 정보 없음: " + requestDto.storeName());
                return ResponseEntity.badRequest().body("❌ 해당 스토어 정보가 없습니다.");
            }

            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // ✅ 모델1 호출 및 결과 저장
            Model1Results result = model1Service.analyzeAndSave(requestDto);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println("❌ 예외 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
        }
    }
}
