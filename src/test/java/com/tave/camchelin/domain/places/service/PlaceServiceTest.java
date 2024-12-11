package com.tave.camchelin.domain.places.service;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.places.service.PlaceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceServiceTest {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceRepository placeRepository;

    @BeforeEach
    void setup() {
        // 이미 데이터가 존재하므로 추가 작업은 하지 않음
    }

    @Test
    void getPlaceById_ShouldReturnPlace_WhenPlaceExists() {
        // Given
        Place place = placeRepository.findById(1L).orElseThrow();
        Long placeId = place.getId();

        // When
        PlaceDto result = placeService.getPlaceById(placeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("안녕유부");
    }

    @Test
    void getPlaceById_ShouldThrowException_WhenPlaceDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> placeService.getPlaceById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("맛집 정보를 찾지 못했습니다.");
    }

    @Test
    void getPlaceByName_ShouldReturnPlace_WhenPlaceExists() {
        // Given
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        String placeName = place.getName();

        // When
        PlaceDto result = placeService.getPlaceByName(placeName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("안녕유부");
    }

    @Test
    void getPlaceByName_ShouldThrowException_WhenPlaceDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> placeService.getPlaceByName("Nonexistent Place"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("맛집 정보를 찾지 못했습니다.");
    }
}
