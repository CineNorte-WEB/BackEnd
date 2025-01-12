package com.tave.camchelin.domain.review_posts.dto.request;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestReviewDto {
        private Long placeId; // 수정할 장소 ID
        private String title; // 수정할 제목
        private String content; // 수정할 내용

    public ReviewPost toEntity(User user, Place place) {
        return ReviewPost.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .place(place)
                .build();
    }

    public static UpdateRequestReviewDto fromEntity(ReviewPost reviewPost) {
        return UpdateRequestReviewDto.builder()
                .title(reviewPost.getTitle())
                .content(reviewPost.getContent())
                .placeId(reviewPost.getPlace() != null ? reviewPost.getPlace().getId() : null)
                .build();
    }
}
