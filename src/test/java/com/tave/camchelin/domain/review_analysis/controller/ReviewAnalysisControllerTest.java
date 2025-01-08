package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.service.ReviewAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewAnalysisControllerTest {
    @Mock
    private PlaceService placeService;

    @Mock
    private ReviewAnalysisService reviewAnalysisService;

    @InjectMocks
    private ReviewAnalysisController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void analyzeAndSave_ShouldReturnSavedReview() {
        // Given
        String storeName = "Test Store";
        String reviewContent = "리뷰 내용입니다.";
        RequestModelDto requestModelDto = new RequestModelDto(storeName, reviewContent);

        PlaceDto placeDto = new PlaceDto(1L, storeName, "음식점", "서울 강남구", "10:00~22:00", 5, 4.5f, "맛있다", "test.jpg", "SNU");
        Place place = Place.builder()
                .id(1L)
                .name(storeName)
                .address("서울 강남구")
                .category("음식점")
                .build();

        ReviewAnalysis expectedAnalysis = ReviewAnalysis.builder()
                .place(place)
                .positiveKeywords(List.of("맛있다"))
                .negativeKeywords(List.of("비싸다"))
                .build();

        // Mock 설정
        when(placeService.getPlaceByName(eq(storeName))).thenReturn(placeDto);
        when(reviewAnalysisService.analyzeAndSave(eq(requestModelDto), any(Place.class))).thenReturn(expectedAnalysis);

        // When
        ResponseEntity<?> response = controller.analyzeAndSave(requestModelDto);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(((ReviewAnalysis) response.getBody()).getPositiveKeywords()).contains("맛있다");
        assertThat(((ReviewAnalysis) response.getBody()).getNegativeKeywords()).contains("비싸다");

        // Verify interactions
        verify(placeService, times(1)).getPlaceByName(eq(storeName));
        verify(reviewAnalysisService, times(1)).analyzeAndSave(eq(requestModelDto), any(Place.class));
    }
}