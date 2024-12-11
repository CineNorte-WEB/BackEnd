package com.tave.camchelin.domain.univs.service;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.univs.dto.UnivDto;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UnivServiceTest {

    @Autowired
    private UnivService univService;

    @Autowired
    private UnivRepository univRepository;

    @BeforeEach
    void setup() {
        // 이미 데이터가 존재하므로 추가 작업은 하지 않음
    }

    @Test
    void getUnivById_ShouldReturnUniv_WhenUnivExists() {
        // Given
        Univ univ = univRepository.findByName("경희대학교").orElseThrow();
        Long univId = univ.getId();

        // When
        UnivDto result = univService.getUnivById(univId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("경희대학교");
    }

    @Test
    void getUnivById_ShouldThrowException_WhenUnivDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> univService.getUnivById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("대학 정보를 찾지 못했습니다.");
    }

    @Test
    void getUnivByName_ShouldReturnUniv_WhenUnivExists() {
        // Given
        Univ univ = univRepository.findByName("경희대학교").orElseThrow();
        String univName = univ.getName();

        // When
        UnivDto result = univService.getUnivByName(univName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("경희대학교");
    }

    @Test
    void getUnivByName_ShouldThrowException_WhenUnivDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> univService.getUnivByName("Nonexistent University"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("대학 정보를 찾지 못했습니다.");
    }

    @Test
    void getAllUnivs_ShouldReturnListOfUnivs() {
        // When
        List<UnivDto> result = univService.getAllUnivs();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(6);
    }

    @Test
    void getPlacesByUnivId_ShouldReturnPlaces_WhenUnivHasPlaces() {
        // Given
        Univ univ = univRepository.findByName("경희대학교").orElseThrow();
        Long univId = univ.getId();

        // When
        List<PlaceDto> result = univService.getPlacesByUnivId(univId);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("안녕유부");
    }

    @Test
    void getPlacesByUnivId_ShouldThrowException_WhenUnivDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> univService.getPlacesByUnivId(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("대학 정보를 찾지 못했습니다.");
    }
}
