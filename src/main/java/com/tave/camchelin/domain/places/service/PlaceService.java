package com.tave.camchelin.domain.places.service;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.review_analysis.repository.Model2ResultsRepository;
import com.tave.camchelin.domain.review_analysis.service.Model2AnalysisService;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final UnivRepository univRepository; // UnivRepository 주입
    private final Model2ResultsRepository model2ResultsRepository;

    private final Model2AnalysisService model2AnalysisService;

    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        List<Model2Results> model2ResultsList = model2ResultsRepository.findByStoreName(place.getName());

        Pair<Model2Results, Map<String, List<String>>> results = model2AnalysisService.processModel2Results(model2ResultsList);


        // PlaceDto로 반환, 메뉴도 포함됨
        return PlaceDto.fromEntity(place, results.getFirst(), results.getSecond());
    }

    @Transactional(readOnly = true)
    public PlaceDto getPlaceByName(String placeName) {
        System.out.println("placeName = " + placeName);
        Place place = placeRepository.findByName(placeName)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        List<Model2Results> model2ResultsList = model2ResultsRepository.findByStoreName(place.getName());

        Pair<Model2Results, Map<String, List<String>>> results = model2AnalysisService.processModel2Results(model2ResultsList);


        // PlaceDto로 반환, 메뉴도 포함됨
        return PlaceDto.fromEntity(place, results.getFirst(), results.getSecond());
    }

    @Transactional(readOnly = true)
    public Univ getUnivEntityByName(String univName) {
        return univRepository.findByName(univName)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));
    }
}
