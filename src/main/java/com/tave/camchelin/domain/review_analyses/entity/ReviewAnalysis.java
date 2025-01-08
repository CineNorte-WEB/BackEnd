package com.tave.camchelin.domain.review_analyses.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_analyses.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "review_keywords") // 새 테이블 이름
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "positive_keywords", columnDefinition = "TEXT")
    private List<String> positiveKeywords; // JSON 형태로 저장

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "negative_keywords", columnDefinition = "TEXT")
    private List<String> negativeKeywords; // JSON 형태로 저장
}
