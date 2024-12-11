package com.tave.camchelin.domain.univs.service;

import com.tave.camchelin.domain.places.dto.PlaceDto;
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
                .map(PlaceDto::fromEntity)
                .collect(Collectors.toList());
    }
}
