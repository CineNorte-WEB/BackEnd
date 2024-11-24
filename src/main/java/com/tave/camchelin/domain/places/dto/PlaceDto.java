package com.tave.camchelin.domain.places.dto;

import com.tave.camchelin.domain.places.entity.Place;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto {
    private Long id;
    private String name;
    private String address;
    private String summary;
    private String imageUrl;
    private Float latitude;
    private Float longitude;

    // PlaceDto -> Place 엔티티로 변환
    public Place toEntity() {
        return Place.builder()
                .name(this.name)
                .address(this.address)
                .summary(this.summary)
                .imageUrl(this.imageUrl)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }

    // Place 엔티티 -> PlaceDto로 변환
    public static PlaceDto fromEntity(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .summary(place.getSummary())
                .imageUrl(place.getImageUrl())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }
}
