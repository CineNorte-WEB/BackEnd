package com.tave.camchelin.domain.review_posts.service;

import com.tave.camchelin.domain.review_keywords.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_keywords.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_keywords.entity.Model1Results;
import com.tave.camchelin.domain.review_keywords.service.Model1AnalysisService;
import com.tave.camchelin.domain.review_keywords.service.Model2AnalysisService;
import com.tave.camchelin.domain.review_posts.dto.request.UpdateRequestReviewDto;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.dto.response.ResponseReviewDto;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.review_posts.repository.ReviewPostRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewPostService {

    private final ReviewPostRepository reviewPostRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final PlaceRepository placeRepository;

    private final Model1AnalysisService model1AnalysisService;
    private final Model2AnalysisService model2AnalysisService;

    // 모든 리뷰 조회
    @Transactional(readOnly = true)
    public Page<ResponseReviewDto> getReviewPosts(Pageable pageable) {
        return reviewPostRepository.findAll(pageable)
                .map(ResponseReviewDto::fromEntity);
    }

    // 특정 리뷰 조회
    @Transactional(readOnly = true)
    public ResponseReviewDto getReviewPostById(Long reviewPostId) {
        ReviewPost reviewPost = reviewPostRepository.findById(reviewPostId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        return ResponseReviewDto.fromEntity(reviewPost);
    }

    // 리뷰 작성
    @Transactional
    public ResponseReviewDto writeReviewPost(Long userId, ReviewPostDto reviewPostDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Community community = communityRepository.findByName("reviewPost")
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티 정보를 찾을 수 없습니다."));
        Place place = placeRepository.findById(reviewPostDto.getPlace().getId())
                .orElseThrow(() -> new IllegalArgumentException("장소 정보를 찾을 수 없습니다."));

        ReviewPost reviewPost = reviewPostDto.toEntity(user, community, place);

        reviewPost = reviewPostRepository.save(reviewPost);

        // 3. 모델1 호출 및 결과 업데이트
        Model1RequestDto model1RequestDto = new Model1RequestDto(
                place.getName(),
                reviewPost.getContent()
        );
        Model1Results model1Result = model1AnalysisService.analyzeAndSave(model1RequestDto);

        // 4. 모델2 호출 및 결과 업데이트 (Positive)
        Model2RequestDto positiveRequestDto = new Model2RequestDto(
                place.getName(),
                model1Result.getPositiveKeywords() // Positive keywords 사용
        );
        model2AnalysisService.analyzeAndSave(positiveRequestDto, "Positive");

        // 5. 모델2 호출 및 결과 업데이트 (Negative)
        Model2RequestDto negativeRequestDto = new Model2RequestDto(
                place.getName(),
                model1Result.getNegativeKeywords() // Negative keywords 사용
        );
        model2AnalysisService.analyzeAndSave(negativeRequestDto, "Negative");


        return ResponseReviewDto.fromEntity(reviewPost);
    }

    // 리뷰 수정
    @Transactional
    public void editReviewPost(Long userId, Long reviewPostId, UpdateRequestReviewDto updateRequestDto) {
        ReviewPost reviewPost = reviewPostRepository.findById(reviewPostId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!reviewPost.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        log.info("수정 전 Place ID: {}", reviewPost.getPlace().getId());
        log.info("수정 요청 Place ID: {}", updateRequestDto.getPlaceId());
        Place newPlace = null;
        if (updateRequestDto.getPlaceId() != null) {
            newPlace = placeRepository.findById(updateRequestDto.getPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("장소 정보를 찾을 수 없습니다."));
        }


        reviewPost.edit(newPlace, updateRequestDto.getTitle(), updateRequestDto.getContent());
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReviewPost(Long userId, Long reviewPostId) {
        ReviewPost reviewPost = reviewPostRepository.findById(reviewPostId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!reviewPost.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        reviewPostRepository.delete(reviewPost);
    }

    // 특정 장소의 리뷰 조회
    @Transactional(readOnly = true)
    public Page<ResponseReviewDto> getReviewsByPlace(Long placeId, Pageable pageable) {
        return reviewPostRepository.findByPlaceId(placeId, pageable)
                .map(ResponseReviewDto::fromEntity);
    }
}
