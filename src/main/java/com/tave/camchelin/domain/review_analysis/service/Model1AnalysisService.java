package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.review_analysis.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model1ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Model1AnalysisService {

    private final CallApiService callApiService;
    private final Model1ResultsRepository model1ResultsRepository;

    public Model1Results analyzeAndSave(Model1RequestDto requestDto) {
        // 모델1 API 호출
        System.out.println("📌 모델1 API 호출 시작");
        Model1ResponseDto responseDto = callApiService.callModel1Api(requestDto);

        if (responseDto == null) {
            throw new RuntimeException("❌ Model1 API가 null을 반환했습니다.");
        }

        System.out.println("✅ 모델1 API 응답 수신: " + responseDto);

        Model1Results model1Results = model1ResultsRepository.findFirstByStoreNameOrderByIdDesc(requestDto.storeName())
                .orElseThrow(() -> new RuntimeException("❌ 해당 StoreName에 대한 데이터가 존재하지 않습니다: " + requestDto.storeName()));

        List<String> updatedPositiveKeywords = new ArrayList<>(model1Results.getPositiveKeywords());
        updatedPositiveKeywords.addAll(responseDto.positiveKeywords()); // 새로운 키워드 추가

        List<String> updatedNegativeKeywords = new ArrayList<>(model1Results.getNegativeKeywords());
        updatedNegativeKeywords.addAll(responseDto.negativeKeywords()); // 새로운 키워드 추가

        // 엔티티 업데이트
        model1Results.setPositiveKeywords(updatedPositiveKeywords);
        model1Results.setNegativeKeywords(updatedNegativeKeywords);

        return model1ResultsRepository.save(model1Results);
    }

    public Model1Results findByStoreName(String storeName) {
        Optional<Model1Results> result = model1ResultsRepository.findFirstByStoreNameOrderByIdDesc(storeName);
        System.out.println("result = " + result);
        return result.orElse(null); // 결과 없으면 null 반환
    }
}
