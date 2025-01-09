package com.tave.camchelin.domain.menus.repository;

import com.tave.camchelin.domain.menus.entity.Menu;
import com.tave.camchelin.domain.places.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByPlace(Place place);
}
