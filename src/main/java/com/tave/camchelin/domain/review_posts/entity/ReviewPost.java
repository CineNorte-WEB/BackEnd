package com.tave.camchelin.domain.review_posts.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_posts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

//    @Column(name = "positive_rating", nullable = false)
//    private Float positiveRating;
//
//    @Column(name = "negative_rating", nullable = false)
//    private Float negativeRating;

    public void edit(Place place, String content) {
        // Place가 변경 가능한 경우
        if (place != null) {
            this.place = place;
        }

        // Content가 null 또는 공백이 아닌 경우에만 변경
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }
}
