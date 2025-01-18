package com.tave.camchelin.domain.places.dto;

import com.tave.camchelin.domain.menus.dto.MenuDto;
import com.tave.camchelin.domain.places.entity.Place;
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
public class PlaceDto {
    private Long id;
    private String name;
    private String category;
    private String address;
    private String hours;
    private int reviewCount;
    private Float rating;
    private String likePoints;
    private String imageUrl;
    private String univName;
    private List<MenuDto> menus;

    private Map<String, Object> representativeSentenceMap;

    // PlaceDto -> Place 엔티티로 변환
    public Place toEntity(Univ univ) {
        return Place.builder()
                .name(this.name)
                .category(this.category)
                .address(this.address)
                .hours(this.hours)
                .reviewCount(this.reviewCount)
                .rating(this.rating)
                .likePoints(this.likePoints)
                .imageUrl(this.imageUrl)
                .univ(univ)
                .build();
    }


    // Place 엔티티 -> PlaceDto로 변환
    public static PlaceDto fromEntity(Place place, Model2Results model2Results, Map<String, List<String>> allSentences) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .category(place.getCategory())
                .address(place.getAddress())
                .hours(place.getHours())
                .reviewCount(place.getReviewCount())
                .rating(place.getRating())
                .likePoints(place.getLikePoints())
                .imageUrl(place.getImageUrl())
                .univName(place.getUniv().getName())
                .menus(place.getMenus().stream()
                        .map(MenuDto::fromEntity)
                        .collect(Collectors.toList()))
                .representativeSentenceMap(Map.of(
                        "topSentence", model2Results != null ? model2Results.getRepresentativeSentence() : null,
                        "positiveSentences", allSentences.getOrDefault("Positive", List.of()),
                        "negativeSentences", allSentences.getOrDefault("Negative", List.of())
                ))
                .build();
    }
}
