package com.tave.camchelin.domain.board_posts.repository;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    Page<BoardPost> findByUserId(Long id, Pageable pageable);
}
