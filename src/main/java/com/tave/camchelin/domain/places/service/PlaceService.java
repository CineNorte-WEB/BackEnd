package com.tave.camchelin.domain.places.service;

import com.tave.camchelin.domain.menus.dto.MenuDto;
import com.tave.camchelin.domain.menus.repository.MenuRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final UnivRepository univRepository; // UnivRepository 주입
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        // PlaceDto로 반환, 메뉴도 포함됨
        return PlaceDto.fromEntity(place);
    }

    @Transactional(readOnly = true)
    public PlaceDto getPlaceByName(String placeName) {
        Place place = placeRepository.findByName(placeName)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        // PlaceDto로 반환, 메뉴도 포함됨
        return PlaceDto.fromEntity(place);
    }

    @Transactional(readOnly = true)
    public Univ getUnivEntityByName(String univName) {
        return univRepository.findByName(univName)
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾지 못했습니다."));
    }
}
