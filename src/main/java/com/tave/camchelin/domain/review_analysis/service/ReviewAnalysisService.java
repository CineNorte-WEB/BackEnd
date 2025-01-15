package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.domain.review_analysis.repository.Model2ResultsRepository;
import com.tave.camchelin.domain.review_analysis.repository.ReviewAnalysisStoreRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewAnalysisService {

    private final Model1ResultsRepository model1Repository;
    private final Model2ResultsRepository model2Repository;
    private final CallApiService callApiService;

    public ReviewAnalysisService(
            Model1ResultsRepository model1Repository,
            Model2ResultsRepository model2Repository,
            CallApiService callApiService
    ) {
        this.model1Repository = model1Repository;
        this.model2Repository = model2Repository;
        this.callApiService = callApiService;
    }

    @Transactional
    public List<Model2Results> analyzeAndSave(RequestModelDto requestDto, Place place) {
        // Step 1: 모델1 호출 및 저장
        ResponseModelDto model1Response = callApiService.callModel1Api(requestDto);

        Model1Results model1Result = Model1Results.builder()
                .storeName(requestDto.storename())
                .positiveKeywords(List.of(model1Response.Positive_Keywords().split(", ")))
                .negativeKeywords(List.of(model1Response.Negative_Keywords().split(", ")))
                .build();
        Model1Results savedModel1Result = model1Repository.save(model1Result);

        // Step 2: 모델2 호출 및 저장
        List<String> model2Keywords = new ArrayList<>();
        model2Keywords.addAll(List.of(model1Response.Positive_Keywords().split(", ")));
        model2Keywords.addAll(List.of(model1Response.Negative_Keywords().split(", ")));

        Model2RequestDto model2Request = new Model2RequestDto(requestDto.storename(), model2Keywords);
        Model2ResponseDto model2Response = callApiService.callModel2Api(model2Request);

        List<Model2Results> savedModel2Results = new ArrayList<>();
        for (Model2ResponseDto.CategoryResult result : model2Response.results()) {
            Model2Results model2Result = Model2Results.builder()
                    .model1Result(savedModel1Result)
                    .storeName(requestDto.storename())
                    .category(result.category())
                    .groupKeywords(List.of(result.groupKeywords().split(", ")))
                    .representativeSentence(result.representativeSentence())
                    .build();
            savedModel2Results.add(model2Repository.save(model2Result));
        }

        return savedModel2Results;
    }
}