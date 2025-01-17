package com.tave.camchelin.domain.univs.service;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.domain.univs.dto.UnivDto;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivService {
    private final UnivRepository univRepository;
    private final Model1ResultsRepository model1ResultsRepository; // ✅ Model1Results 추가


    public UnivDto getUnivById(Long id) {
        Univ univ = univRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));

        return UnivDto.fromEntity(univ, model1ResultsRepository);
    }

    public UnivDto getUnivByName(String name) {
        Univ univ = univRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));

        return UnivDto.fromEntity(univ, model1ResultsRepository);
    }

    public List<UnivDto> getAllUnivs() {
        List<Univ> univs = univRepository.findAll();
        return univs.stream()
                .map(univ -> UnivDto.fromEntity(univ, model1ResultsRepository)) // ✅ Model1ResultsRepository 추가
                .collect(Collectors.toList());
    }

    public List<PlaceDto> getPlacesByUnivId(Long univId) {
        Univ univ = univRepository.findById(univId)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));

        return univ.getPlaces().stream()
                .map(place -> {
                    Model1Results model1Results = model1ResultsRepository.findFirstByStoreNameOrderByIdDesc(place.getName()).orElse(null);
                    return PlaceDto.fromEntity(place, model1Results);
                })
                .collect(Collectors.toList());
    }
}
