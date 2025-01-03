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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "univ_id", nullable = false)
    private Univ univ;

    private String menu;

    private Integer price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

//    @Column(name = "positive_rating", nullable = false)
//    private Float positiveRating;
//
//    @Column(name = "negative_rating", nullable = false)
//    private Float negativeRating;

    public void edit(String menu, Integer price, String content) {
        if (menu != null && !menu.isBlank()) {
            this.menu = menu;
        }
        if (price != null && price >= 0) { // 가격은 음수가 될 수 없도록 검증
            this.price = price;
        }
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }
}
