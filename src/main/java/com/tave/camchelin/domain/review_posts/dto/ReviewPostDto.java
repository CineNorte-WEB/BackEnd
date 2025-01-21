package com.tave.camchelin.domain.review_posts.dto;

import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPostDto {

    private Long id;
    private User user;
    private Community community;
    private Place place;
    private String title;
    private String content;
    private String image;

    public ReviewPost toEntity(User user, Community community, Place place) {
        return ReviewPost.builder()
                .user(user)
                .community(community)
                .place(place)
                .title(this.title)
                .content(this.content)
                .image(this.image)
                .build();
    }

    public static ReviewPostDto fromEntity(ReviewPost reviewPost) {
        return ReviewPostDto.builder()
                .id(reviewPost.getId())
                .user(reviewPost.getUser())
                .place(reviewPost.getPlace())
                .title(reviewPost.getTitle())
                .content(reviewPost.getContent())
                .image(reviewPost.getImage())
                .build();
    }
}
