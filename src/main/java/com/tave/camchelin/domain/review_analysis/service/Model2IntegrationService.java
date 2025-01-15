package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.repository.ReviewAnalysisStoreRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Model2IntegrationService {

    private final ReviewAnalysisStoreRepository repository;
    private final CallApiService callApiService;

    public Model2IntegrationService(ReviewAnalysisStoreRepository repository, CallApiService callApiService) {
        this.repository = repository;
        this.callApiService = callApiService;
    }

    public List<ReviewAnalysis> analyzeAndSave(Model2RequestDto requestDto, Place place) {
        // 모델 API 호출
        Model2ResponseDto responseDto = callApiService.callModel2Api(requestDto);

        // 결과를 DB에 저장
        List<ReviewAnalysis> savedReviews = new ArrayList<>();
        for (Model2ResponseDto.CategoryResult result : responseDto.results()) {
            ReviewAnalysis reviewAnalysis = ReviewAnalysis.builder()
                    .place(place)
                    .name(requestDto.storename())
                    .positiveKeywords(List.of(result.groupKeywords().split(", ")))
                    .negativeKeywords(List.of(result.representativeSentence())) // 예: 대체 처리
                    .build();
            savedReviews.add(repository.save(reviewAnalysis));
        }
        return savedReviews;
    }
}
