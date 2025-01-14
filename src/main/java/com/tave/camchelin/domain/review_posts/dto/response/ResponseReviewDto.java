package com.tave.camchelin.domain.review_posts.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseReviewDto {

    private Long id; // 리뷰 ID
    private String title; // 리뷰 제목
    private String content; // 리뷰 내용
    private String userNickname; // 작성자 닉네임
    private Long placeId; // 장소 ID
    private String placeName; // 장소 이름
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 생성 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt; // 수정 시간

    // 엔티티를 DTO로 변환
    public static ResponseReviewDto fromEntity(ReviewPost reviewPost) {
        User user = reviewPost.getUser();
        Place place = reviewPost.getPlace();

        return ResponseReviewDto.builder()
                .id(reviewPost.getId())
                .title(reviewPost.getTitle())
                .content(reviewPost.getContent())
                .userNickname(user != null ? user.getNickname() : null)
                .placeId(place != null ? place.getId() : null)
                .placeName(place != null ? place.getName() : null)
                .createdAt(reviewPost.getCreatedAt())
                .updatedAt(reviewPost.getUpdatedAt())
                .build();
    }
}
