package com.tave.camchelin.domain.review_keywords.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_keywords.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_keywords.entity.Model1Results;
import com.tave.camchelin.domain.review_keywords.service.Model1AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> analyzeReview(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("📌 모델1 분석 요청 도착: " + requestData);

            // ✅ JSON 데이터에서 "storename" 추출
            String storeName = (String) requestData.get("storename");
            if (storeName == null || storeName.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ 'storename'이 필요합니다.");
            }

            // ✅ JSON 데이터에서 "review" 추출 (리스트 or 문자열 처리)
            Object reviewObj = requestData.get("review");
            String review;

            if (reviewObj instanceof String) {
                review = (String) reviewObj; // 정상적으로 문자열로 받음
            } else if (reviewObj instanceof List<?>) {
                // 혹시라도 `Array`로 인식되면, 첫 번째 요소를 `String`으로 변환
                List<?> reviewList = (List<?>) reviewObj;
                review = reviewList.isEmpty() ? "" : reviewList.get(0).toString();
            } else {
                return ResponseEntity.badRequest().body("❌ 'review' 필드가 올바른 문자열 형식이 아닙니다.");
            }

            // ✅ Place 조회 (DB에서 장소 정보 확인)
            PlaceDto placeDto = placeService.getPlaceByName(storeName);
            if (placeDto == null) {
                return ResponseEntity.badRequest().body("❌ '" + storeName + "' 가게 정보가 없습니다.");
            }

            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName()));

            // ✅ 모델1 호출 및 결과 저장
            Model1RequestDto requestDto = new Model1RequestDto(storeName, review);
            Model1Results result = model1Service.analyzeAndSave(requestDto);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println("❌ 예외 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error occurred: " + e.getMessage());
        }
    }
}