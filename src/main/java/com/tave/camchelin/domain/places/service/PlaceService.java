package com.tave.camchelin.domain.places.service;

import com.tave.camchelin.domain.menus.repository.MenuRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final UnivRepository univRepository; // UnivRepository 주입
    private final MenuRepository menuRepository;
    private final Model1ResultsRepository model1ResultsRepository; // ✅ Model1Results 추가

    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        // ✅ Model1 키워드 조회
        Model1Results model1Results = model1ResultsRepository.findFirstByStoreNameOrderByIdDesc(place.getName()).orElse(null);

        // PlaceDto로 반환, 메뉴도 포함됨
        return PlaceDto.fromEntity(place, model1Results);
    }

    @Transactional(readOnly = true)
    public PlaceDto getPlaceByName(String placeName) {
        System.out.println("placeName = " + placeName);
        Place place = placeRepository.findByName(placeName)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));
        System.out.println("=======================================================");

        // ✅ Model1 키워드 조회
        Model1Results model1Results = model1ResultsRepository.findFirstByStoreNameOrderByIdDesc(placeName).orElse(null);

        // PlaceDto로 반환, 메뉴도 포함됨
        return PlaceDto.fromEntity(place, model1Results);
    }

    @Transactional(readOnly = true)
    public Univ getUnivEntityByName(String univName) {
        return univRepository.findByName(univName)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));
    }
}
