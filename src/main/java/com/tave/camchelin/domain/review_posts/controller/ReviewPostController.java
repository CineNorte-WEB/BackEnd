package com.tave.camchelin.domain.review_posts.controller;

import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.service.ReviewPostService;
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
    public ResponseEntity<ReviewPostDto> writeReviewPost(@RequestBody ReviewPostDto reviewPostDto) {
        ReviewPostDto createdReviewPostDto = reviewPostService.writeReviewPost(reviewPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewPostDto);
    }

    // 리뷰 수정
    @PutMapping("/{review_post_id}/edit")
    public ResponseEntity<Void> editReviewPost(
            @PathVariable("review_post_id") Long reviewPostId,
            @RequestBody ReviewPostDto reviewPostDto) {
        reviewPostService.editReviewPost(reviewPostId, reviewPostDto);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{review_post_id}")
    public ResponseEntity<Void> deleteReviewPost(@PathVariable("review_post_id") Long reviewPostId) {
        reviewPostService.deleteReviewPost(reviewPostId);
        return ResponseEntity.ok().build();
    }

    // 특정 맛집 리뷰 목록 조회
    @GetMapping("/place/{place_id}")
    public ResponseEntity<List<ReviewPostDto>> getReviewsByPlace(@PathVariable("place_id") Long placeId) {
        List<ReviewPostDto> reviews = reviewPostService.getReviewsByPlace(placeId);
        return ResponseEntity.ok(reviews);
    }
}
