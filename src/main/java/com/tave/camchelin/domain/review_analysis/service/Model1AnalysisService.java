package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model1ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Model1AnalysisService {

    private final CallApiService callApiService;
    private final Model1ResultsRepository repository;

    public Model1AnalysisService(CallApiService callApiService, Model1ResultsRepository repository) {
        this.callApiService = callApiService;
        this.repository = repository;
    }

    public Model1Results analyzeAndSave(Model1RequestDto requestDto, Place place) {
        // 모델1 API 호출
        Model1ResponseDto responseDto = callApiService.callModel1Api(requestDto);

        // 결과를 저장
        Model1Results model1Result = Model1Results.builder()
                .storeName(requestDto.storename())
                .positiveKeywords(List.of(responseDto.Positive_Keywords().split(", ")))
                .negativeKeywords(List.of(responseDto.Negative_Keywords().split(", ")))
                .build();

        return repository.save(model1Result);
    }

    public Model1Results findByStoreName(String storeName) {
        Optional<Model1Results> result = repository.findByStoreName(storeName);
        return result.orElse(null); // 결과 없으면 null 반환
    }
}
