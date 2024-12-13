package com.tave.camchelin.domain.board_posts.service;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardPostServiceTest {

    @Autowired
    private BoardPostService boardPostService;

    @Autowired
    private BoardPostRepository boardPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UnivRepository univRepository;

    @BeforeEach
    void setup() {
        // 이미 데이터가 존재하므로 추가 작업은 하지 않음
        Univ univ = univRepository.findByName("경희대학교")
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾을 수 없습니다."));

        User user = User.builder()
                .username("testUser")
                .password("password")
                .nickname("nickname")
                .univ(univ)
                .build();
        userRepository.save(user);
    }

    @Test
    void getBoardPosts_ShouldReturnBoardPosts_WhenBoardPostsExist2() {
        // Given
        User user = userRepository.findByUsername("testUser").orElseThrow();
        Community community = communityRepository.findByName("boardPost").orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "Test Title", "Test Content", user, community);

        // 엔티티 저장
        boardPostRepository.save(boardPostDto.toEntity(user, community));

        // 데이터 확인 (Optional)
        List<BoardPost> savedBoardPosts = boardPostRepository.findAll();
        System.out.println("==== 저장된 BoardPost 데이터 ====");
        savedBoardPosts.forEach(post -> {
            System.out.println("ID: " + post.getId());
            System.out.println("Title: " + post.getTitle());
            System.out.println("Content: " + post.getContent());
            System.out.println("User: " + (post.getUser() != null ? post.getUser().getUsername() : "null"));
            System.out.println("Community: " + (post.getCommunity() != null ? post.getCommunity().getName() : "null"));
            System.out.println("---------------------------------");
        });

        // When
        List<BoardPostDto> result = boardPostService.getBoardPosts();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Title");
    }

    @Test
    void getBoardPosts_ShouldReturnBoardPosts_WhenBoardPostsExist() {
        // Given
        User user = userRepository.findByUsername("testUser").orElseThrow();
        Community community = communityRepository.findById(1L).orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "Test Title", "Test Content", user, community);
        boardPostRepository.save(boardPostDto.toEntity(user, community));


        // When
        List<BoardPostDto> result = boardPostService.getBoardPosts();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Title");
    }

    @Test
    void getBoardPostById_ShouldReturnBoardPost_WhenBoardPostExists() {
        // Given
        User user = userRepository.findByUsername("testUser").orElseThrow();
        Community community = communityRepository.findByName("boardPost").orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "Test Title", "Test Content", user, community);
        BoardPost boardPost = boardPostDto.toEntity(user, community);

        boardPostRepository.save(boardPost);
        Long boardPostId = boardPost.getId();

        // When
        BoardPostDto result = boardPostService.getBoardPostById(boardPostId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getContent()).isEqualTo("Test Content");
    }

    @Test
    void getBoardPostById_ShouldThrowException_WhenBoardPostDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> boardPostService.getBoardPostById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @Test
    void writeBoardPost_ShouldCreateBoardPost_WhenValidDataProvided() {
        // Given
        User user = userRepository.findByUsername("testUser").orElseThrow();
        Community community = communityRepository.findByName("boardPost").orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "New Title", "New Content", user, community);

        // When
        BoardPostDto result = boardPostService.writeBoardPost(boardPostDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getContent()).isEqualTo("New Content");
    }

    @Test
    void writeBoardPost_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        Community community = communityRepository.findByName("boardPost").orElseThrow();
        User user = User.builder()
                .id(999L) // 존재하지 않는 ID
                .username("nonexistentUser")
                .password("password")
                .nickname("nickname")
                .univ(null) // Univ는 null로 설정
                .build();

        BoardPostDto boardPostDto = BoardPostDto.builder()
                .title("New Title")
                .content("New Content")
                .user(user)
                .community(community)
                .build();
        // When & Then
        assertThatThrownBy(() -> boardPostService.writeBoardPost(boardPostDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유저 정보를 찾을 수 없습니다.");
    }

    @Test
    void editBoardPost_ShouldEditBoardPost_WhenValidDataProvided() {
        // Given
        User user = userRepository.findByUsername("testUser").orElseThrow();
        Community community = communityRepository.findByName("boardPost").orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "Old Title", "Old Content", user, community);
        BoardPost boardPost = boardPostRepository.save(boardPostDto.toEntity(user, community));
        Long boardPostId = boardPost.getId();

        BoardPostDto boardPostDto2 = new BoardPostDto(boardPostId, "Updated Title", "Updated Content", user, community);

        // When
        boardPostService.editBoardPost(boardPostId, boardPostDto2);

        // Then
        BoardPost updatedBoardPost = boardPostRepository.findById(boardPostId).orElseThrow();
        assertThat(updatedBoardPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedBoardPost.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void deleteBoardPost_ShouldDeleteBoardPost_WhenBoardPostExists() {
        // Given
        User user = userRepository.findByUsername("testUser").orElseThrow();
        Community community = communityRepository.findByName("boardPost").orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "Title to Delete", "Content", user, community);
        BoardPost boardPost = boardPostRepository.save(boardPostDto.toEntity(user, community));
        Long boardPostId = boardPost.getId();

        // When
        boardPostService.deleteBoardPost(boardPostId);

        // Then
        assertThat(boardPostRepository.findById(boardPostId)).isEmpty();
    }

    @Test
    void deleteBoardPost_ShouldThrowException_WhenBoardPostDoesNotExist() {
        // When & Then
        assertThatThrownBy(() -> boardPostService.deleteBoardPost(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }
}
