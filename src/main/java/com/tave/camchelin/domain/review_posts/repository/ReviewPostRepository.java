package com.tave.camchelin.domain.review_posts.repository;

import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {
    List<ReviewPost> findByPlaceId(Long placeId);
    List<ReviewPost> findByUserId(Long id);
}
