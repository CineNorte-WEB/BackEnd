package com.tave.camchelin.domain.univs.dto;

import com.tave.camchelin.domain.places.dto.PlaceDto;
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
    private Float latitude;
    private Float longitude;
    private List<PlaceDto> places;

    public Univ toEntity() {
        return Univ.builder()
                .name(this.name)
                .imageUrl(this.imageUrl)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }

    public static UnivDto fromEntity(Univ univ) {
        return UnivDto.builder()
                .id(univ.getId())
                .name(univ.getName())
                .imageUrl(univ.getImageUrl())
                .latitude(univ.getLatitude())
                .longitude(univ.getLongitude())
                .places(univ.getPlaces().stream()
                        .map(PlaceDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}