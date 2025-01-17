package com.tave.camchelin.domain.review_analysis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.service.PlaceService;
import com.tave.camchelin.domain.review_analysis.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.service.Model1AnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Model1Controller.class)
public class Model1ControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private Model1AnalysisService model1Service;
//
//    @MockBean
//    private PlaceService placeService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testAnalyzeReview_Success() throws Exception {
//        // GIVEN: Mock 데이터 준비
//        String storeName = "Test Store";
//        String review = "The food was great and the service was excellent.";
//        Model1RequestDto requestDto = new Model1RequestDto(storeName, review);
//
//        // PlaceDto 생성 (DB 조회 시 반환될 값)
//        PlaceDto placeDto = PlaceDto.builder()
//                .id(1L)
//                .name(storeName)
//                .category("Restaurant")
//                .address("123 Main St")
//                .hours("9AM - 9PM")
//                .reviewCount(10)
//                .rating(4.5f)
//                .likePoints("Great service")
//                .imageUrl("http://example.com/image.jpg")
//                .univName("Sample University")
//                .menus(List.of()) // 메뉴는 없을 수도 있음
//                .build();
//
//        // Place 엔티티 변환
//        Place place = placeDto.toEntity(null);  // `univ` 필드가 필요 없을 경우 null로 처리
//
//        // Model1Results 생성
//        Model1Results model1Results = Model1Results.builder()
//                .id(1L)
//                .storeName(storeName)
//                .positiveKeywords(List.of("Great", "Delicious"))
//                .negativeKeywords(List.of("Expensive"))
//                .build();
//
//        // WHEN: PlaceService와 Model1AnalysisService Mock 설정
//        when(placeService.getPlaceByName(storeName)).thenReturn(placeDto);
//        when(model1Service.analyzeAndSave(any(Model1RequestDto.class), any(Place.class))).thenReturn(model1Results);
//
//        // THEN: API 호출 및 검증
//        mockMvc.perform(post("/model1/generate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"store_name\": \"Test Store\", \"review\": \"The food was great and the service was excellent.\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.storeName").value(storeName))
//                .andExpect(jsonPath("$.positiveKeywords[0]").value("Great"))
//                .andExpect(jsonPath("$.negativeKeywords[0]").value("Expensive"));
//
//        // Mock 서비스가 호출되었는지 확인
//        verify(placeService, times(1)).getPlaceByName(storeName);
//        verify(model1Service, times(1)).analyzeAndSave(any(Model1RequestDto.class), any(Place.class));
//    }
//
//    @Test
//    public void testAnalyzeReview_Fail_StoreNotFound() throws Exception {
//        // GIVEN: 가게 정보가 없을 때
//        Model1RequestDto requestDto = new Model1RequestDto("Unknown Store", "Tasteless food");
//
//        when(placeService.getPlaceByName("Unknown Store")).thenReturn(null);
//
//        // THEN: 400 BAD REQUEST 검증
//        mockMvc.perform(post("/model1/generate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isBadRequest());
//    }
}
