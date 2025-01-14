package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.service.ModelIntegrationService;
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
    private ModelIntegrationService modelIntegrationService;

    @InjectMocks
    private ReviewAnalysisController reviewAnalysisController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processAndSave_ShouldReturnSavedReviews() {
        // Given
        RequestModelDto requestModelDto = new RequestModelDto("Test Store", "This is a test review.");
        PlaceDto placeDto = new PlaceDto(1L, "Test Store", "Restaurant", "Seoul", "10:00~22:00", 5, 4.5f, "Nice", "image.jpg", "SNU", null);
        Place place = Place.builder().id(1L).name("Test Store").build();

        ReviewAnalysis expectedReview = ReviewAnalysis.builder()
                .place(place)
                .name("Test Store")
                .positiveKeywords(List.of("Clean"))
                .negativeKeywords(List.of("Expensive"))
                .build();

        when(placeService.getPlaceByName("Test Store")).thenReturn(placeDto);
        when(modelIntegrationService.processAndSave(requestModelDto, place)).thenReturn(List.of(expectedReview));

        // When
        ResponseEntity<?> response = reviewAnalysisController.processAndSave(requestModelDto);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(List.class);

        List<?> savedReviews = (List<?>) response.getBody();
        assertThat(savedReviews).hasSize(1);

        verify(placeService, times(1)).getPlaceByName("Test Store");
        verify(modelIntegrationService, times(1)).processAndSave(requestModelDto, place);
    }
}