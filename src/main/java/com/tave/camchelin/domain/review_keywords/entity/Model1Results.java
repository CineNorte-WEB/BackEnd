package com.tave.camchelin.domain.review_keywords.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.review_keywords.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "model1_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Model1Results extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "positive_keywords", columnDefinition = "TEXT")
    private List<String> positiveKeywords;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "negative_keywords", columnDefinition = "TEXT")
    private List<String> negativeKeywords;
}