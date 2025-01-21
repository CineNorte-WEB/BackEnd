package com.tave.camchelin.domain.univs.service;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.review_keywords.entity.Model2Results;
import com.tave.camchelin.domain.review_keywords.repository.Model2ResultsRepository;
import com.tave.camchelin.domain.review_keywords.service.Model2AnalysisService;
import com.tave.camchelin.domain.univs.dto.UnivDto;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivService {
    private final UnivRepository univRepository;
    private final Model2ResultsRepository model2ResultsRepository;
    private final Model2AnalysisService model2AnalysisService;

    public UnivDto getUnivById(Long id) {
        Univ univ = univRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));

        return UnivDto.fromEntity(univ);
    }

    public UnivDto getUnivByName(String name) {
        Univ univ = univRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));

        return UnivDto.fromEntity(univ);
    }

    public List<UnivDto> getAllUnivs() {
        List<Univ> univs = univRepository.findAll();
        return univs.stream()
                .map(UnivDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PlaceDto> getPlacesByUnivId(Long univId) {
        Univ univ = univRepository.findById(univId)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));

        return univ.getPlaces().stream()
                .map(place -> {
                    // Model2Results 데이터 처리
                    List<Model2Results> model2ResultsList = model2ResultsRepository.findByStoreName(place.getName());
                    Pair<Model2Results, Map<String, Map<String, String>>> results = model2AnalysisService.processModel2Results(model2ResultsList);

                    // PlaceDto 생성
                    return PlaceDto.fromEntity(place, results.getFirst(), results.getSecond());
                })
                .collect(Collectors.toList());
    }
}
