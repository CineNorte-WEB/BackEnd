package com.tave.camchelin.domain.review_keywords.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.review_keywords.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "model2_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Model2Results extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String sentiment;

    @Column(nullable = false)
    private String category;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "group_keywords", columnDefinition = "TEXT")
    private List<String> groupKeywords;

    @Column(name = "representative_sentence", columnDefinition = "TEXT")
    private String representativeSentence;
}