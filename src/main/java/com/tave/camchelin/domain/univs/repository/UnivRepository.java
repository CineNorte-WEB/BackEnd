package com.tave.camchelin.domain.univs.repository;

import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnivRepository extends JpaRepository<Univ, Long> {
    Optional<Univ> findByName(String name);
}
