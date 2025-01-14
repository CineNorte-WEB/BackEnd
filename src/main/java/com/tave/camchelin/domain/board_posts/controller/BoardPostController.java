package com.tave.camchelin.domain.board_posts.controller;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.dto.response.ResponseBoardDto;
import com.tave.camchelin.domain.board_posts.dto.request.UpdateRequestBoardDto;
import com.tave.camchelin.domain.board_posts.service.BoardPostService;
import com.tave.camchelin.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<ResponseBoardDto>> getBoardPosts(
            @RequestParam(defaultValue = "0") int page, // 기본 페이지 번호는 0
            @RequestParam(defaultValue = "5") int size // 기본 페이지 크기는 5
    ) {
        Page<ResponseBoardDto> boardPosts = boardPostService.getBoardPosts(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return ResponseEntity.ok(boardPosts);
    }


    // 특정 게시글 조회
    @GetMapping("/{board_post_id}")
    public ResponseEntity<ResponseBoardDto> getBoardPost(@PathVariable("board_post_id") Long boardPostId) {
        ResponseBoardDto boardPost = boardPostService.getBoardPostById(boardPostId);
        return ResponseEntity.ok(boardPost);
    }

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<ResponseBoardDto> writeBoardPost(
            @RequestHeader("Authorization") String token,
            @RequestBody BoardPostDto boardPostDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        ResponseBoardDto createdBoardPostDto = boardPostService.writeBoardPost(userId, boardPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoardPostDto);
    }

    // 게시글 수정
    @PutMapping("/{board_post_id}/edit")
    public ResponseEntity<Void> editBoardPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("board_post_id") Long boardPostId,
            @RequestBody UpdateRequestBoardDto updateRequestDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        boardPostService.editBoardPost(userId, boardPostId, updateRequestDto);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{board_post_id}")
    public ResponseEntity<Void> deleteBoardPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("board_post_id") Long boardPostId) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        boardPostService.deleteBoardPost(userId, boardPostId);
        return ResponseEntity.ok().build();
    }
}
