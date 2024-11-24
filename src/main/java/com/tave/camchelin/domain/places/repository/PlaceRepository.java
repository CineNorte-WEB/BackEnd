package com.tave.camchelin.domain.places.repository;

import com.tave.camchelin.domain.places.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
