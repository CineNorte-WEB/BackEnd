package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.review_analysis.repository.Model2ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Model2AnalysisService {

    private final Model2ResultsRepository model2ResultsRepository;
    private final CallApiService callApiService;


    @Transactional
    public void analyzeAndSave(Model2RequestDto requestDto, String sentiment) {
        // 모델2 API 호출
        Model2ResponseDto responseDto = callApiService.callModel2Api(requestDto);
        if (responseDto == null || responseDto.results() == null) {
            throw new RuntimeException("Model2 API response is null or empty");
        }

        // 결과를 DB에 저장 (기존 엔티티 업데이트)
        for (Model2ResponseDto.CategoryResult result : responseDto.results()) {
            // sentiment, storename, category가 모두 일치하는 기존 데이터 검색
            Model2Results model2Result = model2ResultsRepository.findByStoreNameAndCategoryAndSentiment(
                    requestDto.storename(), result.category(), sentiment);
            if (model2Result == null) {
                throw new IllegalStateException("해당 엔티티를 찾을 수 없습니다. storeName: "
                        + requestDto.storename() + ", category: " + result.category() + ", sentiment: " + sentiment);
            }

            // 기존 데이터 업데이트
            if (result.groupKeywords() != null && !result.groupKeywords().trim().isEmpty()) {
                model2Result.getGroupKeywords().addAll(List.of(result.groupKeywords().split(", ")));
            }
            model2Result.setRepresentativeSentence(result.representativeSentence());
            model2ResultsRepository.save(model2Result);
        }
    }
}
