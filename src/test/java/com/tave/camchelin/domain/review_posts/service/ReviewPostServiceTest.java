package com.tave.camchelin.domain.review_posts.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReviewPostServiceTest {

    @Autowired
    private ReviewPostService reviewPostService;

    @Autowired
    private ReviewPostRepository reviewPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnivRepository univRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        // Test 데이터 세팅
        Univ univ = univRepository.findByName("경희대학교")
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾을 수 없습니다."));

        User user = User.builder()
                .email("testUser")
                .password("password")
                .nickname("nickname")
                .univ(univ)
                .build();
        userRepository.save(user);
    }

    @Test
    void writeReviewPost_ShouldSaveReviewPost() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        Community community = communityRepository.findByName("reviewPost").orElseThrow();
        Univ univ = univRepository.findById(3L).orElseThrow();

        ReviewPostDto reviewPostDto = new ReviewPostDto(null, user, community, place, univ, "Test Menu", 10000, "Test Content");

        // When
        ReviewPostDto savedReview = reviewPostService.writeReviewPost(user.getId(),reviewPostDto);

        // Then
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getContent()).isEqualTo("Test Content");
        assertThat(reviewPostRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void getReviewPosts_ShouldReturnAllReviews() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        Community community = communityRepository.findByName("reviewPost").orElseThrow();
        Univ univ = univRepository.findById(3L).orElseThrow();

        reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu1", 10000, "Content1"));
        reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu2", 20000, "Content2"));

        // When
        List<ReviewPostDto> reviewPosts = reviewPostService.getReviewPosts();

        // Then
        assertThat(reviewPosts).isNotNull();
        assertThat(reviewPosts.size()).isEqualTo(2);
    }

    @Test
    void getReviewPostById_ShouldReturnSpecificReview() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        Community community = communityRepository.findByName("reviewPost").orElseThrow();
        Univ univ = univRepository.findById(3L).orElseThrow();

        ReviewPost savedReview = reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu1", 10000, "Content1"));

        // When
        ReviewPostDto review = reviewPostService.getReviewPostById(savedReview.getId());

        // Then
        assertThat(review).isNotNull();
        assertThat(review.getContent()).isEqualTo("Content1");
    }

    @Test
    void editReviewPost_ShouldUpdateReviewDetails() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        Community community = communityRepository.findByName("reviewPost").orElseThrow();
        Univ univ = univRepository.findById(3L).orElseThrow();

        ReviewPost savedReview = reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu1", 10000, "Content1"));

        ReviewPostDto updatedDto = ReviewPostDto.builder()
                .menu("Updated Menu")
                .price(15000)
                .content("Updated Content")
                .build();

        // When
        reviewPostService.editReviewPost(user.getId(), savedReview.getId(), updatedDto);

        // Then
        ReviewPost updatedReview = reviewPostRepository.findById(savedReview.getId()).orElseThrow();
        assertThat(updatedReview.getMenu()).isEqualTo("Updated Menu");
        assertThat(updatedReview.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void deleteReviewPost_ShouldRemoveReview() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        Community community = communityRepository.findByName("reviewPost").orElseThrow();
        Univ univ = univRepository.findById(3L).orElseThrow();

        ReviewPost savedReview = reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu1", 10000, "Content1"));

        // When
        reviewPostService.deleteReviewPost(user.getId(), savedReview.getId());

        // Then
        assertThat(reviewPostRepository.findById(savedReview.getId())).isEmpty();
    }

    @Test
    void getReviewsByPlace_ShouldReturnReviewsForSpecificPlace() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findByName("안녕유부").orElseThrow();
        Community community = communityRepository.findByName("reviewPost").orElseThrow();
        Univ univ = univRepository.findById(3L).orElseThrow();

        reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu1", 10000, "Content1"));
        reviewPostRepository.save(new ReviewPost(null, user, community, place, univ, "Menu2", 15000, "Content2"));

        // When
        List<ReviewPostDto> reviews = reviewPostService.getReviewsByPlace(place.getId());

        // Then
        assertThat(reviews).isNotNull();
        assertThat(reviews.size()).isEqualTo(2);
    }
}
