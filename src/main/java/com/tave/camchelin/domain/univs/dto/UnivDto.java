package com.tave.camchelin.domain.univs.dto;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.repository.Model1ResultsRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnivDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String address;
    private List<PlaceDto> places;

    public Univ toEntity() {
        return Univ.builder()
                .name(this.name)
                .imageUrl(this.imageUrl)
                .address(this.address)
                .build();
    }

    public static UnivDto fromEntity(Univ univ, Model1ResultsRepository model1ResultsRepository) {
        return UnivDto.builder()
                .id(univ.getId())
                .name(univ.getName())
                .imageUrl(univ.getImageUrl())
                .address(univ.getAddress())
                .places(univ.getPlaces() != null ?
                        univ.getPlaces().stream()
                                .map(place -> {
                                    Model1Results results = model1ResultsRepository.findByStoreName(place.getName())
                                            .orElse(null);
                                    return PlaceDto.fromEntity(place, results);
                                })
                                .collect(Collectors.toList())
                        : null)
                .build();
    }

}
