package com.tave.camchelin.domain.board_posts.controller;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.service.BoardPostService;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import com.tave.camchelin.domain.users.service.UserService;
import com.tave.camchelin.global.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board_posts")
@RequiredArgsConstructor
public class BoardPostController {

    private final BoardPostService boardPostService;
    private final UserService userService;

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

    @PostMapping("/write")
    public ResponseEntity<BoardPostDto> writeBoardPost(
            @RequestHeader("Authorization") String token,
            @RequestBody BoardPostDto boardPostDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        BoardPostDto createdBoardPostDto = boardPostService.writeBoardPost(userId, boardPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoardPostDto);
    }


    // 게시글 수정
    @PutMapping("/{board_post_id}/edit")
    public ResponseEntity<Void> editBoardPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("board_post_id") Long boardPostId,
            @RequestBody BoardPostDto boardPostDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        boardPostService.editBoardPost(userId, boardPostId, boardPostDto); // userId 추가
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{board_post_id}")
    public ResponseEntity<Void> deleteBoardPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("board_post_id") Long boardPostId) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        boardPostService.deleteBoardPost(userId, boardPostId); // userId 추가
        return ResponseEntity.ok().build();
    }
}
