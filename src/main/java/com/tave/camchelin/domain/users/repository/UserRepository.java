package com.tave.camchelin.domain.users.repository;

import com.tave.camchelin.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    //Optional<User> findByNickName(String nickname);

    //boolean existsByNickName(String nickname);

    Optional<User> findByRefreshToken(String refreshToke);
}
