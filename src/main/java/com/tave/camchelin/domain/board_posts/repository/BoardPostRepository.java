package com.tave.camchelin.domain.board_posts.repository;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {


}
