package com.tave.camchelin.domain.review_posts.controller;

import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.service.ReviewPostService;
import com.tave.camchelin.domain.users.service.UserService;
import com.tave.camchelin.global.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review_posts")
@RequiredArgsConstructor
public class ReviewPostController {

    private final ReviewPostService reviewPostService;
    private final UserService userService;

    // 리뷰 목록 조회
    @GetMapping
    public ResponseEntity<List<ReviewPostDto>> getReviewPosts() {
        List<ReviewPostDto> reviewPosts = reviewPostService.getReviewPosts();
        return ResponseEntity.ok(reviewPosts);
    }

    // 특정 리뷰 조회
    @GetMapping("/{review_post_id}")
    public ResponseEntity<ReviewPostDto> getReviewPost(@PathVariable("review_post_id") Long reviewPostId) {
        ReviewPostDto reviewPost = reviewPostService.getReviewPostById(reviewPostId);
        return ResponseEntity.ok(reviewPost);
    }

    // 리뷰 작성
    @PostMapping("/write")
    public ResponseEntity<ReviewPostDto> writeReviewPost(
            @RequestHeader("Authorization") String token,
            @RequestBody ReviewPostDto reviewPostDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        ReviewPostDto createdReviewPostDto = reviewPostService.writeReviewPost(userId, reviewPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewPostDto);
    }

    // 리뷰 수정
    @PutMapping("/{review_post_id}/edit")
    public ResponseEntity<Void> editReviewPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("review_post_id") Long reviewPostId,
            @RequestBody ReviewPostDto reviewPostDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        reviewPostService.editReviewPost(userId, reviewPostId, reviewPostDto);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{review_post_id}")
    public ResponseEntity<Void> deleteReviewPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("review_post_id") Long reviewPostId) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        reviewPostService.deleteReviewPost(userId, reviewPostId);
        return ResponseEntity.ok().build();
    }

    // 특정 맛집 리뷰 목록 조회
    @GetMapping("/place/{place_id}")
    public ResponseEntity<List<ReviewPostDto>> getReviewsByPlace(@PathVariable("place_id") Long placeId) {
        List<ReviewPostDto> reviews = reviewPostService.getReviewsByPlace(placeId);
        return ResponseEntity.ok(reviews);
    }
}
