package com.tave.camchelin.domain.bookmarks.repository;

import com.tave.camchelin.domain.bookmarks.entity.Bookmark;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(Long userId);
    Optional<Bookmark> findByUserAndPlace(User user, Place place);
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}