package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.review_analysis.repository.Model2ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Model2AnalysisService {

    private final Model2ResultsRepository repository;
    private final CallApiService callApiService;

    public Model2AnalysisService(Model2ResultsRepository repository, CallApiService callApiService) {
        this.repository = repository;
        this.callApiService = callApiService;
    }

    public List<Model2Results> analyzeAndSave(Model2RequestDto requestDto, Place place, Model1Results model1Result) {
        // 모델 API 호출
        Model2ResponseDto responseDto = callApiService.callModel2Api(requestDto);
        if (responseDto == null || responseDto.results() == null) {
            throw new RuntimeException("Model2 API response is null or empty");
        }

        // 결과를 DB에 저장
        List<Model2Results> savedReviews = new ArrayList<>();
        for (Model2ResponseDto.CategoryResult result : responseDto.results()) {
            Model2Results reviewAnalysis = Model2Results.builder()
                    .model1Result(model1Result)
                    .storeName(requestDto.storename())
                    .category(result.category())
                    .groupKeywords(List.of(result.groupKeywords().split(", ")))
                    .representativeSentence(result.representativeSentence())
                    .build();
            savedReviews.add(repository.save(reviewAnalysis));
        }
        return savedReviews;
    }
}
