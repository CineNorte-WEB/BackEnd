package com.tave.camchelin.domain.univs.dto;

import com.tave.camchelin.domain.univs.entity.Univ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
