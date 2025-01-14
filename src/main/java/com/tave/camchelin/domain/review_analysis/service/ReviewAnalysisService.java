package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.repository.ReviewAnalysisStoreRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewAnalysisService {
    /*
    public ResponseModelDto analysis(RequestModelDto requestModelDto){
        return CallApiService.mentApi(requestModelDto);
    }
    */
    private final ReviewAnalysisStoreRepository repository;

    public ReviewAnalysisService(ReviewAnalysisStoreRepository repository) {
        this.repository = repository;
    }

    //모델 호출 및 결과 저장
    public ReviewAnalysis analyzeAndSave(RequestModelDto requestModelDto, Place place) {

        //모델 api 호출
        ResponseModelDto responseModelDto =  CallApiService.callModel1Api(requestModelDto);

        //분석 결과 저장
        ReviewAnalysis reviewAnalysis = ReviewAnalysis.builder()
                .place(place)
                .positiveKeywords(List.of(responseModelDto.Positive_Keywords().split(",")))
                .negativeKeywords(List.of(responseModelDto.Negative_Keywords().split(",")))
                .build();

        return repository.save(reviewAnalysis);
    }
}
