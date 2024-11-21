package com.tave.camchelin.domain.places.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.schools.entity.School;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "places")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String address;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;
}

