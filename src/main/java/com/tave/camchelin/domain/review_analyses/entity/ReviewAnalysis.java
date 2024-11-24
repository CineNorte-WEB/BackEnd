package com.tave.camchelin.domain.review_analyses.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_analyses")
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

    @Column(nullable = false)
    private Float reviewPositiveRating;

    @Column(nullable = false)
    private Float reviewNegativeCount;
}

