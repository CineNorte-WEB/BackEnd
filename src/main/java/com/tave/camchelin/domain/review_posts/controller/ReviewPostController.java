package com.tave.camchelin.domain.review_posts.controller;

import com.tave.camchelin.domain.review_posts.dto.request.UpdateRequestReviewDto;
import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.dto.response.ResponseReviewDto;
import com.tave.camchelin.domain.review_posts.service.ReviewPostService;
import com.tave.camchelin.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 모든 리뷰 조회
    @GetMapping
    public ResponseEntity<Page<ResponseReviewDto>> getReviewPosts(
            @RequestParam(defaultValue = "0") int page, // 기본 페이지 번호는 0
            @RequestParam(defaultValue = "5") int size // 기본 페이지 크기는 5
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ResponseReviewDto> reviewPosts = reviewPostService.getReviewPosts(pageable);
        return ResponseEntity.ok(reviewPosts);
    }

    // 특정 리뷰 조회
    @GetMapping("/{review_post_id}")
    public ResponseEntity<ResponseReviewDto> getReviewPost(@PathVariable("review_post_id") Long reviewPostId) {
        ResponseReviewDto reviewPost = reviewPostService.getReviewPostById(reviewPostId);
        return ResponseEntity.ok(reviewPost);
    }

    // 리뷰 작성
    @PostMapping("/write")
    public ResponseEntity<ResponseReviewDto> writeReviewPost(
            @RequestHeader("Authorization") String token,
            @RequestBody ReviewPostDto reviewPostDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        ResponseReviewDto createdReviewPostDto = reviewPostService.writeReviewPost(userId, reviewPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReviewPostDto);
    }

    // 리뷰 수정
    @PutMapping("/{review_post_id}/edit")
    public ResponseEntity<Void> editReviewPost(
            @RequestHeader("Authorization") String token,
            @PathVariable("review_post_id") Long reviewPostId,
            @RequestBody UpdateRequestReviewDto updateRequestDto) {
        Long userId = userService.extractUserIdFromToken(token); // JWT에서 userId 추출
        reviewPostService.editReviewPost(userId, reviewPostId, updateRequestDto);
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

    // 특정 장소의 리뷰 목록 조회
    @GetMapping("/place/{place_id}")
    public ResponseEntity<Page<ResponseReviewDto>> getReviewsByPlace(
            @PathVariable("place_id") Long placeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ResponseReviewDto> reviews = reviewPostService.getReviewsByPlace(placeId, pageable);
        return ResponseEntity.ok(reviews);
    }
}
