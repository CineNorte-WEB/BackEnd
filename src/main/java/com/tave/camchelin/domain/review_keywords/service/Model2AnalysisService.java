package com.tave.camchelin.domain.review_keywords.service;

import com.tave.camchelin.domain.review_keywords.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_keywords.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_keywords.entity.Model2Results;
import com.tave.camchelin.domain.review_keywords.repository.Model2ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                List<String> newKeywords = List.of(result.groupKeywords().split(", "));
                model2Result.setGroupKeywords(new ArrayList<>(newKeywords)); // 기존 키워드 대체
            }
            model2Result.setRepresentativeSentence(result.representativeSentence());
            model2ResultsRepository.save(model2Result);
        }
    }


    public Pair<Model2Results, Map<String, Map<String, String>>> processModel2Results(List<Model2Results> model2ResultsList) {
        // 긍정 및 부정 문장 분리
        Map<String, String> positiveSentencesByCategory = model2ResultsList.stream()
                .filter(result -> "Positive".equals(result.getSentiment()))
                .filter(result -> result.getRepresentativeSentence() != null && !result.getRepresentativeSentence().isEmpty())
                .collect(Collectors.toMap(
                        Model2Results::getCategory, // 카테고리
                        Model2Results::getRepresentativeSentence // 대표 문장
                ));

        Map<String, String> negativeSentencesByCategory = model2ResultsList.stream()
                .filter(result -> "Negative".equals(result.getSentiment()))
                .filter(result -> result.getRepresentativeSentence() != null && !result.getRepresentativeSentence().isEmpty())
                .collect(Collectors.toMap(
                        Model2Results::getCategory, // 카테고리를 key로 설정
                        Model2Results::getRepresentativeSentence // 대표 문장만 추출
                ));

        // Map 생성
        Map<String, Map<String, String>> allSentencesBySentiment = Map.of(
                "Positive", positiveSentencesByCategory,
                "Negative", negativeSentencesByCategory
        );

        // 가장 긴 groupKeywords를 가진 Model2Results 선택
        Model2Results topResult = model2ResultsList.stream()
                .max(Comparator.comparingInt(m -> m.getGroupKeywords().size()))
                .orElse(null);

        return Pair.of(topResult, allSentencesBySentiment);
    }
}
