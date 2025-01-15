package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.repository.ReviewAnalysisStoreRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelIntegrationService {

    private final CallApiService callApiService;
    private final ReviewAnalysisStoreRepository repository;

    public ModelIntegrationService(CallApiService callApiService, ReviewAnalysisStoreRepository repository) {
        this.callApiService = callApiService;
        this.repository = repository;
    }

    public List<ReviewAnalysis> processAndSave(RequestModelDto requestDto, Place place) {
        // Step 1: 모델1 호출
        ResponseModelDto model1Response = callApiService.callModel1Api(requestDto);

        // Step 2: 모델2 입력 데이터 생성
        List<String> model2Keywords = List.of(model1Response.Positive_Keywords(), model1Response.Negative_Keywords());
        Model2RequestDto model2Request = new Model2RequestDto(requestDto.storename(), model2Keywords);

        // Step 3: 모델2 호출
        Model2ResponseDto model2Response = callApiService.callModel2Api(model2Request);

        // Step 4: 결과 저장
        List<ReviewAnalysis> savedResults = new ArrayList<>();
        for (Model2ResponseDto.CategoryResult result : model2Response.results()) {
            ReviewAnalysis reviewAnalysis = ReviewAnalysis.builder()
                    .place(place)
                    .name(requestDto.storename())
                    .positiveKeywords(List.of(result.groupKeywords().split(", ")))
                    .negativeKeywords(List.of(result.representativeSentence())) // 예: 대체 처리
                    .build();
            savedResults.add(repository.save(reviewAnalysis));
        }

        return savedResults;
    }



}
