package com.tave.camchelin.domain.users.service;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.dto.response.ResponseBoardDto;
import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.bookmarks.entity.Bookmark;
import com.tave.camchelin.domain.bookmarks.repository.BookmarkRepository;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.dto.response.ResponseReviewDto;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.review_posts.repository.ReviewPostRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.dto.request.UpdateRequestUserDto;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnivRepository univRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BoardPostRepository boardPostRepository;

    @Autowired
    private ReviewPostRepository reviewPostRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @BeforeEach
    void setup() {
        Univ univ = univRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("대학 정보가 존재하지 않습니다."));
        UserDto userDto = new UserDto(null, "testUser2", "password", "nickname", 1L);
        User user = userDto.toEntity(univ);
        userRepository.save(user);
    }

    @Test
    void registerUser_ShouldRegisterSuccessfully_WhenValidDataProvided() {
        // Given
        UserDto userDto = new UserDto(null, "newUser", "password", "newNickname", 1L);

        // When
        UserDto result = userService.registerUser(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("newUser");
        assertThat(userRepository.findByEmail("newUser")).isPresent();
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        // Given
        UserDto userDto = new UserDto(null, "testUser", "password", "nickname", 1L);

        // When & Then
        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 사용자입니다.");
    }

    @Test
    void getUserProfile_ShouldReturnUserProfile_WhenUserExists() {
        // Given
        User existingUser = userRepository.findByEmail("testUser").orElseThrow();
        Long userId = existingUser.getId();

        // When
        UserDto result = userService.getUserProfile(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("testUser");
    }

    @Test
    void getUserProfile_ShouldThrowException_WhenUserDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> userService.getUserProfile(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자 정보를 찾지 못했습니다.");
    }

    @Test
    void updateUser_ShouldUpdateUserDetails_WhenValidDataProvided() {
        // Given
        User existingUser = userRepository.findByEmail("testUser").orElseThrow();
        Long userId = existingUser.getId();
        UpdateRequestUserDto userDto = new UpdateRequestUserDto(userId, "updatedUser", "newNickname", 1L);

        // When
        UserDto result = userService.updateUser(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("updatedUser");
        assertThat(result.getNickname()).isEqualTo("newNickname");
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        UpdateRequestUserDto userDto = new UpdateRequestUserDto(999L, "nonexistentUser", "nickname", 1L);

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자 정보를 찾지 못했습니다.");
    }

    @Test
    void getUserBookmarks_ShouldReturnBookmarks_WhenUserHasBookmarks() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findById(1L).orElseThrow();
        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .place(place)
                .build());

        // When
        List<PlaceDto> bookmarks = userService.getUserBookmarks(user.getId());

        // Then
        assertThat(bookmarks).isNotNull().hasSize(1);
        assertThat(bookmarks.get(0).getName()).isEqualTo("안녕유부");
    }

    @Test
    void addBookmark_ShouldAddBookmarkSuccessfully_WhenValidDataProvided() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Long userId = user.getId();
        Long placeId = 1L;

        // When
        userService.addBookmark(userId, placeId);

        // Then
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);
        assertThat(bookmarks).hasSize(1);
        assertThat(bookmarks.get(0).getPlace().getId()).isEqualTo(placeId);
    }

    @Test
    void removeBookmark_ShouldRemoveBookmarkSuccessfully_WhenBookmarkExists() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Place place = placeRepository.findById(1L).orElseThrow();
        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .place(place)
                .build());


        // When
        userService.removeBookmark(user.getId(), place.getId());

        // Then
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(user.getId());
        assertThat(bookmarks).isEmpty();
    }

    @Test
    void removeBookmark_ShouldThrowException_WhenBookmarkDoesNotExist() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Long userId = user.getId();
        Long placeId = 999L;

        // When & Then
        assertThatThrownBy(() -> userService.removeBookmark(userId, placeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("맛집 정보를 찾지 못했습니다.");
    }



    @Test
    void getUserBoardPosts_ShouldReturnBoardPosts_WhenUserHasBoardPosts_WithPagination() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Community community = communityRepository.findById(1L).orElseThrow();

        // 게시글 5개 생성
        for (int i = 1; i <= 5; i++) {
            boardPostRepository.save(BoardPost.builder()
                    .user(user)
                    .community(community)
                    .title("Test Board Post " + i)
                    .content("Content of Board Post " + i)
                    .build());
        }

        // 페이징 설정: 1페이지, 3개씩
        Pageable pageable = PageRequest.of(0, 3);

        // When
        Page<ResponseBoardDto> boardPosts = userService.getUserBoardPosts(user.getId(), pageable);

        // Then
        assertThat(boardPosts).isNotNull();
        assertThat(boardPosts.getContent()).hasSize(3); // 첫 페이지의 데이터 수 확인
        assertThat(boardPosts.getTotalElements()).isEqualTo(5); // 전체 게시글 수 확인
        assertThat(boardPosts.getContent().get(0).getTitle()).isEqualTo("Test Board Post 1"); // 첫 번째 게시글 확인
        assertThat(boardPosts.getContent().get(1).getTitle()).isEqualTo("Test Board Post 2"); // 두 번째 게시글 확인
        assertThat(boardPosts.getContent().get(2).getTitle()).isEqualTo("Test Board Post 3"); // 세 번째 게시글 확인
    }


    @Test
    void getUserBoardPosts_ShouldReturnEmptyPage_WhenUserHasNoBoardPosts() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();

        Pageable pageable = PageRequest.of(0, 3);

        // When
        Page<ResponseBoardDto> boardPosts = userService.getUserBoardPosts(user.getId(), pageable);

        // Then
        assertThat(boardPosts).isNotNull();
        assertThat(boardPosts.getContent()).isEmpty();
        assertThat(boardPosts.getTotalElements()).isEqualTo(0);
    }

    @Test
    void getUserReviewPosts_ShouldReturnReviewPosts_WhenUserHasReviewPosts_WithPagination() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();
        Community community = communityRepository.findById(2L).orElseThrow();
        Place place = placeRepository.findById(1L).orElseThrow();

        for (int i = 1; i <= 2; i++) {
            reviewPostRepository.save(ReviewPost.builder()
                    .user(user)
                    .community(community)
                    .place(place)
                    .content("Review Content " + i)
                    .build());
        }

        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<ResponseReviewDto> reviewPosts = userService.getUserReviewPosts(user.getId(), pageable);

        // Then
        assertThat(reviewPosts).isNotNull();
        assertThat(reviewPosts.getContent()).hasSize(2);
        assertThat(reviewPosts.getContent().get(0).getContent()).isEqualTo("Review Content 1");
        assertThat(reviewPosts.getContent().get(1).getContent()).isEqualTo("Review Content 2");
    }

    @Test
    void getUserReviewPosts_ShouldReturnEmptyPage_WhenUserHasNoReviewPosts() {
        // Given
        User user = userRepository.findByEmail("testUser").orElseThrow();

        Pageable pageable = PageRequest.of(0, 3);

        // When
        Page<ResponseReviewDto> reviewPosts = userService.getUserReviewPosts(user.getId(), pageable);

        // Then
        assertThat(reviewPosts).isNotNull();
        assertThat(reviewPosts.getContent()).isEmpty();
        assertThat(reviewPosts.getTotalElements()).isEqualTo(0);
    }

}
