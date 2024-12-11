package com.tave.camchelin.domain.board_posts.controller;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board_posts")
@RequiredArgsConstructor
public class BoardPostController {

    private final BoardPostService boardPostService;

    // 게시글 목록 전체 조회
    @GetMapping
    public ResponseEntity<List<BoardPostDto>> getBoardPosts() {
        List<BoardPostDto> boardPosts = boardPostService.getBoardPosts();
        return ResponseEntity.ok(boardPosts);
    }

    // 특정 게시글 조회
    @GetMapping("/{board_post_id}")
    public ResponseEntity<BoardPostDto> getBoardPost(@PathVariable("board_post_id") Long boardPostId) {
        BoardPostDto boardPost = boardPostService.getBoardPostById(boardPostId);
        return ResponseEntity.ok(boardPost);
    }

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<BoardPostDto> writeBoardPost(@RequestBody BoardPostDto boardPostDto) {
        BoardPostDto createdBoardPostDto = boardPostService.writeBoardPost(boardPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoardPostDto);
    }

    // 게시글 수정
    @PutMapping("/{board_post_id}/edit")
    public ResponseEntity<Void> editBoardPost(
            @PathVariable("board_post_id") Long boardPostId,
            @RequestBody BoardPostDto boardPostDto) {
        boardPostService.editBoardPost(boardPostId, boardPostDto);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{board_post_id}")
    public ResponseEntity<Void> deleteBoardPost(@PathVariable("board_post_id") Long boardPostId) {
        boardPostService.deleteBoardPost(boardPostId);
        return ResponseEntity.ok().build();
    }
}
