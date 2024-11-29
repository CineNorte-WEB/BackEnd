package com.tave.camchelin.domain.communities.repository;

import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String name);
}
