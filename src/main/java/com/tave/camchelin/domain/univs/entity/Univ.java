package com.tave.camchelin.domain.univs.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.places.entity.Place;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "univs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Univ{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "univ")
    private List<Place> places = new ArrayList<>();
}
