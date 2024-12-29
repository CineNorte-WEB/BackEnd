package com.tave.camchelin.domain.comments.service;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.comments.dto.CommentDto;
import com.tave.camchelin.domain.comments.entity.Comment;
import com.tave.camchelin.domain.comments.repository.CommentRepository;
import com.tave.camchelin.domain.comments.service.CommentService;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardPostRepository boardPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnivRepository univRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @BeforeEach
    void setUp() {
        // 사용자와 게시글 샘플 데이터 생성
        Univ univ = univRepository.findByName("경희대학교")
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾을 수 없습니다."));

        User user = User.builder()
                .email("testUser")
                .password("password")
                .nickname("nickname")
                .univ(univ)
                .build();
        userRepository.save(user);

        Community community = communityRepository.findByName("boardPost").orElseThrow();

        BoardPostDto boardPostDto = new BoardPostDto(null, "Test Title", "Test Content", user, community);
        boardPostRepository.save(boardPostDto.toEntity(user, community));
    }

    @Test
    void writeComment_ShouldSaveComment() {
        // Given
        BoardPost post = boardPostRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        CommentDto commentDto = CommentDto.builder()
                .content("Test Comment")
                .user(user)
                .build();

        // When
        CommentDto savedComment = commentService.writeComment(post.getId(), commentDto);

        // Then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Test Comment");
        assertThat(commentRepository.findAll().size()).isEqualTo(1);
    }


    @Test
    void writeRecomment_ShouldSaveRecomment() {
        // Given
        BoardPost post = boardPostRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment parentComment = commentRepository.save(Comment.builder()
                .content("Parent Comment")
                .boardPost(post)
                .user(user)
                .build());

        CommentDto recommentDto = CommentDto.builder()
                .content("Child Comment")
                .user(user)
                .parentComment(parentComment)
                .build();

        // When
        CommentDto savedRecomment = commentService.writeComment(post.getId(), recommentDto);

        // Then
        assertThat(savedRecomment).isNotNull();
        assertThat(savedRecomment.getContent()).isEqualTo("Child Comment");
        assertThat(commentRepository.findAll().size()).isEqualTo(2);
        assertThat(savedRecomment.getParentComment().getId()).isEqualTo(parentComment.getId());
    }

    @Test
    void getCommentsByPost_ShouldReturnComments() {
        // Given
        BoardPost post = boardPostRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("Comment 1")
                .boardPost(post)
                .user(user)
                .build());

        commentRepository.save(Comment.builder()
                .content("Comment 2")
                .boardPost(post)
                .user(user)
                .build());

        // When
        List<CommentDto> comments = commentService.getCommentsByPost(post.getId());

        // Then
        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0).getContent()).isEqualTo("Comment 1");
        assertThat(comments.get(1).getContent()).isEqualTo("Comment 2");
    }

    @Test
    void editComment_ShouldUpdateComment() {
        // Given
        BoardPost post = boardPostRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment comment = commentRepository.save(Comment.builder()
                .content("Original Comment")
                .boardPost(post)
                .user(user)
                .build());

        CommentDto updatedCommentDto = CommentDto.builder()
                .content("Updated Comment")
                .build();

        // When
        CommentDto updatedComment = commentService.editComment(comment.getId(), updatedCommentDto);

        // Then
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
        assertThat(commentRepository.findById(comment.getId()).get().getContent()).isEqualTo("Updated Comment");
    }


    @Test
    void deleteComment_ShouldRemoveComment() {
        // Given
        BoardPost post = boardPostRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment comment = commentRepository.save(Comment.builder()
                .content("Test Comment")
                .boardPost(post)
                .user(user)
                .build());


        // Reload entity to ensure Hibernate initializes relationships
        Comment commentToDelete = commentRepository.findById(comment.getId()).orElseThrow();

        // Ensure childComments is not null
        assertThat(commentToDelete.getChildComments()).isNotNull();

        // When
        commentService.deleteComment(commentToDelete.getId());

        // Then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Autowired
    private EntityManager entityManager;

    @Test
    void deleteParentComment_ShouldRemoveRecomments() {
        // Given
        BoardPost post = boardPostRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment parentComment = commentRepository.save(Comment.builder()
                .content("Parent Comment")
                .boardPost(post)
                .user(user)
                .build());

        Comment childComment = commentRepository.save(Comment.builder()
                .content("Child Comment")
                .boardPost(post)
                .user(user)
                .parentComment(parentComment)
                .build());

        // 플러시 및 클리어
        entityManager.flush();
        entityManager.clear();

        // When
        commentService.deleteComment(parentComment.getId());

        // Then
        assertThat(commentRepository.findById(parentComment.getId())).isEmpty();
        assertThat(commentRepository.findById(childComment.getId())).isEmpty(); // 자식 댓글도 삭제되었는지 확인
    }
}
