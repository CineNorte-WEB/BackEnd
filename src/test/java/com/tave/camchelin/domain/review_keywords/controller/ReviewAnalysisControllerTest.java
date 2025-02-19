package com.tave.camchelin.domain.review_keywords.controller;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewAnalysisControllerTest {

//    @Mock
//    private PlaceService placeService;
//
//    @Mock
//    private ModelIntegrationService modelIntegrationService;
//
//    @InjectMocks
//    private ReviewAnalysisController reviewAnalysisController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void processAndSave_ShouldReturnSavedReviews() {
//        // Given
//        RequestModelDto requestModelDto = new RequestModelDto("Test Store", "This is a test review.");
//        PlaceDto placeDto = new PlaceDto(1L, "Test Store", "Restaurant", "Seoul", "10:00~22:00", 5, 4.5f, "Nice", "image.jpg", "SNU", null);
//        Place place = Place.builder().id(1L).name("Test Store").build();
//
//        ReviewAnalysis expectedReview = ReviewAnalysis.builder()
//                .place(place)
//                .name("Test Store")
//                .positiveKeywords(List.of("Clean"))
//                .negativeKeywords(List.of("Expensive"))
//                .build();
//
//        when(placeService.getPlaceByName("Test Store")).thenReturn(placeDto);
//        when(modelIntegrationService.processAndSave(requestModelDto, place)).thenReturn(List.of(expectedReview));
//
//        // When
//        ResponseEntity<?> response = reviewAnalysisController.processAndSave(requestModelDto);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).isInstanceOf(List.class);
//
//        List<?> savedReviews = (List<?>) response.getBody();
//        assertThat(savedReviews).hasSize(1);
//
//        verify(placeService, times(1)).getPlaceByName("Test Store");
//        verify(modelIntegrationService, times(1)).processAndSave(requestModelDto, place);
//    }
}