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
    private Univ univ;
    private String menu;
    private Integer price;
    private String content;

    public ReviewPost toEntity(User user, Community community, Place place, Univ univ) {
        return ReviewPost.builder()
                .user(user)
                .community(community)
                .place(place)
                .univ(univ)
                .menu(this.menu)
                .price(this.price)
                .content(this.content)
                .build();
    }

    public static ReviewPostDto fromEntity(ReviewPost reviewPost) {
        return ReviewPostDto.builder()
                .id(reviewPost.getId())
                .user(reviewPost.getUser())
                .community(reviewPost.getCommunity())
                .place(reviewPost.getPlace())
                .univ(reviewPost.getUniv())
                .menu(reviewPost.getMenu())
                .price(reviewPost.getPrice())
                .content(reviewPost.getContent())
                .build();
    }
}
