package com.tave.camchelin.domain.review_analyses.entity;

import com.tave.camchelin.domain.BaseEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_post_id", nullable = false)
    private ReviewPost reviewPost;

    @Column(name = "analysis_result", nullable = false, columnDefinition = "TEXT")
    private String analysisResult;
}

