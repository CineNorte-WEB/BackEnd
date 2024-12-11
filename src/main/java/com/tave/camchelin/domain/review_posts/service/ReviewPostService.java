package com.tave.camchelin.domain.review_posts.service;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.review_posts.repository.ReviewPostRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewPostService {

    private final ReviewPostRepository reviewPostRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final PlaceRepository placeRepository;
    private final UnivRepository univRepository;

    // 모든 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewPostDto> getReviewPosts() {
        List<ReviewPost> reviewPosts = reviewPostRepository.findAll();
        return reviewPosts.stream()
                .map(ReviewPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 리뷰 조회
    @Transactional(readOnly = true)
    public ReviewPostDto getReviewPostById(Long reviewPostId) {
        ReviewPost reviewPost = reviewPostRepository.findById(reviewPostId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        return ReviewPostDto.fromEntity(reviewPost);
    }

    // 리뷰 작성
    @Transactional
    public ReviewPostDto writeReviewPost(ReviewPostDto reviewPostDto) {
        User user = userRepository.findById(reviewPostDto.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Community community = communityRepository.findByName("reviewPost")
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티 정보를 찾을 수 없습니다."));
        Place place = placeRepository.findById(reviewPostDto.getPlace().getId())
                .orElseThrow(() -> new IllegalArgumentException("장소 정보를 찾을 수 없습니다."));
        Univ univ = univRepository.findById(reviewPostDto.getUniv().getId())
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾을 수 없습니다."));

        ReviewPost reviewPost = ReviewPost.builder()
                .user(user)
                .community(community)
                .place(place)
                .univ(univ)
                .menu(reviewPostDto.getMenu())
                .price(reviewPostDto.getPrice())
                .content(reviewPostDto.getContent())
                .build();

        reviewPost = reviewPostRepository.save(reviewPost);
        return ReviewPostDto.fromEntity(reviewPost);
    }

    // 리뷰 수정
    @Transactional
    public void editReviewPost(Long reviewPostId, ReviewPostDto reviewPostDto) {
        ReviewPost reviewPost = reviewPostRepository.findById(reviewPostId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        reviewPost.edit(
                reviewPostDto.getMenu(),
                reviewPostDto.getPrice(),
                reviewPostDto.getContent()
        );

        reviewPostRepository.save(reviewPost);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReviewPost(Long reviewPostId) {
        ReviewPost reviewPost = reviewPostRepository.findById(reviewPostId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        reviewPostRepository.delete(reviewPost);
    }

    // 특정 장소의 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewPostDto> getReviewsByPlace(Long placeId) {
        List<ReviewPost> reviews = reviewPostRepository.findByPlaceId(placeId);
        return reviews.stream()
                .map(ReviewPostDto::fromEntity)
                .collect(Collectors.toList());
    }
}

