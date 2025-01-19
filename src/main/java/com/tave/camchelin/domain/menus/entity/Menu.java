package com.tave.camchelin.domain.menus.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.places.entity.Place;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String description;
}

