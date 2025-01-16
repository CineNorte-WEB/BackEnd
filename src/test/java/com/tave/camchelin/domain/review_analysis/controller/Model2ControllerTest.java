package com.tave.camchelin.domain.review_analysis.controller;

import static org.assertj.core.api.Assertions.assertThat;

class Model2ControllerTest {

//    @Mock
//    private PlaceService placeService;
//
//    @Mock
//    private Model2IntegrationService model2IntegrationService;
//
//    @InjectMocks
//    private Model2Controller model2Controller;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void analyzeAndSave_ShouldReturnSavedReviews() {
//        // Given
//        String storeName = "Test Store";
//        List<String> keywords = List.of("맛있다", "친절하다");
//        Model2RequestDto requestDto = new Model2RequestDto(storeName, keywords);
//
//        PlaceDto placeDto = new PlaceDto(1L, storeName, "음식점", "서울 강남구", "10:00~22:00", 5, 4.5f, "맛있다", "test.jpg", "SNU", null);
//        Place place = Place.builder()
//                .id(1L)
//                .name(storeName)
//                .address("서울 강남구")
//                .category("음식점")
//                .build();
//
//        List<ReviewAnalysis> expectedAnalyses = List.of(
//                ReviewAnalysis.builder()
//                        .place(place)
//                        .name(storeName)
//                        .positiveKeywords(List.of("맛있다"))
//                        .negativeKeywords(List.of("비싸다"))
//                        .build()
//        );
//
//        // Mock 설정
//        when(placeService.getPlaceByName(eq(storeName))).thenReturn(placeDto);
//        when(model2IntegrationService.analyzeAndSave(eq(requestDto), any(Place.class))).thenReturn(expectedAnalyses);
//
//        // When
//        ResponseEntity<?> response = model2Controller.analyzeAndSave(requestDto);
//
//        // Then
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).isNotNull();
//        assertThat((List<?>) response.getBody()).hasSize(1);
//        assertThat(((List<ReviewAnalysis>) response.getBody()).get(0).getPositiveKeywords()).contains("맛있다");
//
//        // Verify interactions
//        verify(placeService, times(1)).getPlaceByName(eq(storeName));
//        verify(model2IntegrationService, times(1)).analyzeAndSave(eq(requestDto), any(Place.class));
//    }
}
