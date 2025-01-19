package com.tave.camchelin.domain.univs.dto;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import com.tave.camchelin.domain.univs.entity.Univ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
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

    public Univ toEntity() {
        return Univ.builder()
                .name(this.name)
                .imageUrl(this.imageUrl)
                .address(this.address)
                .build();
    }

    public static UnivDto fromEntity(Univ univ) {
        return UnivDto.builder()
                .id(univ.getId())
                .name(univ.getName())
                .imageUrl(univ.getImageUrl())
                .address(univ.getAddress())
                .build();
    }
}
