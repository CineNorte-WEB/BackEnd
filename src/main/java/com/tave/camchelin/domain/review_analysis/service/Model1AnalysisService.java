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

    public Model1Results analyzeAndSave(Model1RequestDto requestDto) {
        // 모델1 API 호출
        System.out.println("📌 모델1 API 호출 시작");
        Model1ResponseDto responseDto = callApiService.callModel1Api(requestDto);

        if (responseDto == null) {
            throw new RuntimeException("❌ Model1 API가 null을 반환했습니다.");
        }

        System.out.println("✅ 모델1 API 응답 수신: " + responseDto);

        // 결과를 저장
        Model1Results model1Result = Model1Results.builder()
                .storeName(requestDto.storeName())
                .positiveKeywords(responseDto.positiveKeywords())  // ✅ List<String> 바로 사용
                .negativeKeywords(responseDto.negativeKeywords())  // ✅ List<String> 바로 사용
                .build();

        return repository.save(model1Result);
    }

    public Model1Results findByStoreName(String storeName) {
        Optional<Model1Results> result = repository.findByStoreName(storeName);
        System.out.println("result = " + result);
        return result.orElse(null); // 결과 없으면 null 반환
    }
}
