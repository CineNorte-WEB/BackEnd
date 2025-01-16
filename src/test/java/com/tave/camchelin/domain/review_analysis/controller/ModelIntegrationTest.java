package com.tave.camchelin.domain.review_analysis.controller;

import com.tave.camchelin.domain.places.entity.Place;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.review_analysis.service.ReviewAnalysisService;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.domain.review_analysis.repository.Model2ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ModelIntegrationTest {
//
//    @MockBean
//    private CallApiService callApiService;
//
//    @InjectMocks
//    private ReviewAnalysisService reviewAnalysisService;
//
//    @MockBean
//    private Model1ResultsRepository model1Repository;
//
//    @MockBean
//    private Model2ResultsRepository model2Repository;
//
//    @Test
//    void analyzeAndSave_ShouldConnectModel1AndModel2Results() {
//        // Given
//        RequestModelDto requestModelDto = new RequestModelDto("Test Store", "Great food");
//        ResponseModelDto model1Response = new ResponseModelDto("Test Store", "Delicious", "Expensive");
//
//        Model1Results savedModel1 = Model1Results.builder()
//                .id(1L)
//                .storeName("Test Store")
//                .positiveKeywords(List.of("Delicious"))
//                .negativeKeywords(List.of("Expensive"))
//                .build();
//
//        Model2RequestDto model2Request = new Model2RequestDto("Test Store", List.of("Delicious", "Expensive"));
//        Model2ResponseDto model2Response = new Model2ResponseDto(
//                "Test Store",
//                List.of(new Model2ResponseDto.CategoryResult("Food", "Delicious", "Best food ever"))
//        );
//
//        when(callApiService.callModel1Api(eq(requestModelDto))).thenReturn(model1Response);
//        when(model1Repository.save(any(Model1Results.class))).thenReturn(savedModel1);
//        when(callApiService.callModel2Api(eq(model2Request))).thenReturn(model2Response);
//
//        // When
//        List<Model2Results> savedModel2Results = reviewAnalysisService.analyzeAndSave(requestModelDto, new Place());
//
//        // Then
//        assertThat(savedModel2Results).isNotNull();
//        assertThat(savedModel2Results).hasSize(1);
//        assertThat(savedModel2Results.get(0).getCategory()).isEqualTo("Food");
//        assertThat(savedModel2Results.get(0).getGroupKeywords()).contains("Delicious");
//    }
}
