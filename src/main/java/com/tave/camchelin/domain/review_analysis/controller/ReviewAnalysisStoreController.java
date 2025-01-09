package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.service.ReviewAnalysisStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review-analysis")
public class ReviewAnalysisStoreController { //분석결과 db에 저장하는 controller

    private final ReviewAnalysisStoreService reviewAnalysisService;
    private final PlaceService placeService;

    public ReviewAnalysisStoreController(ReviewAnalysisStoreService reviewAnalysisService, PlaceService placeService) {
        this.reviewAnalysisService = reviewAnalysisService;
        this.placeService = placeService;
    }

    /**
     * 리뷰 분석 결과 저장
     */
    @PostMapping
    public ResponseEntity<?> saveReviewAnalysis(@RequestBody Map<String, Object> requestData) {
        try {
            // 요청 데이터 파싱
            Long placeId = Long.parseLong(requestData.get("placeId").toString());
            PlaceDto placeDto = placeService.getPlaceById(placeId); // DTO로 조회
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName())); // DTO에서 엔티티 변환

            // 키워드 추출
            @SuppressWarnings("unchecked")
            List<String> positiveKeywords = (List<String>) requestData.get("positiveKeywords");

            @SuppressWarnings("unchecked")
            List<String> negativeKeywords = (List<String>) requestData.get("negativeKeywords");

            // 데이터 저장
            ReviewAnalysis savedReview = reviewAnalysisService.saveReviewAnalysis(place, positiveKeywords, negativeKeywords);
            return ResponseEntity.ok(savedReview);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid placeId format");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    /**
     * 모든 리뷰 분석 결과 조회
     */
    @GetMapping
    public ResponseEntity<List<ReviewAnalysis>> getAllReviewAnalyses() {
        List<ReviewAnalysis> analyses = reviewAnalysisService.getAllReviewAnalyses();
        return ResponseEntity.ok(analyses);
    }

    /**
     * 특정 장소의 리뷰 분석 결과 조회
     */
    @GetMapping("/{placeId}")
    public ResponseEntity<?> getReviewAnalysisByPlace(@PathVariable Long placeId) {
        try {
            PlaceDto placeDto = placeService.getPlaceById(placeId); // DTO로 조회
            Place place = placeDto.toEntity(placeService.getUnivEntityByName(placeDto.getUnivName())); // DTO에서 엔티티 변환
            List<ReviewAnalysis> analyses = reviewAnalysisService.getReviewAnalysisByPlace(place);
            return ResponseEntity.ok(analyses);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }
}
