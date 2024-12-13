package com.tave.camchelin.domain.comments.service;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.comments.dto.CommentDto;
import com.tave.camchelin.domain.comments.entity.Comment;
import com.tave.camchelin.domain.comments.repository.CommentRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto writeComment(Long postId, CommentDto commentDto) {
        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(commentDto.getUser().getId()) // @AuthenticationPrincipal 사용으로 바꾸기)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        Comment parentComment = null;
        if (commentDto.getParentComment() != null) {
            parentComment = commentRepository.findById(commentDto.getParentComment().getId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
        }

        // 댓글 엔티티 생성
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .boardPost(post) // 연관 관계 설정
                .user(user) // 작성자 정보 설정
                .parentComment(parentComment) // 대댓글인 경우 설정
                .build();

        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPost(Long postId) {
        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        List<Comment> comments = commentRepository.findByBoardPostAndParentCommentIsNull(post);
        return comments.stream()
                .map(CommentDto::fromEntity)
                .toList();
    }

    @Transactional
    public CommentDto editComment(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.edit(commentDto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return CommentDto.fromEntity(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.getChildComments().size();

        commentRepository.delete(comment);
    }
//
//    public CommentDto writeRecomment(Long parentCommentId, CommentDto commentDto) {
//        BoardPost post = boardPostRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//        Comment parentComment = commentRepository.findById(parentCommentId)
//                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
//
//        Comment recomment = Comment.builder()
//                .content(commentDto.getContent())
//                .user(commentDto.getUser())
//                .parentComment(commentDto.getParentComment())
//                .build();
//
//        commentRepository.save(recomment);
//        return CommentDto.fromEntity(recomment);
//    }
//
//    public List<CommentDto> getRecommentsByComment(Long commentId) {
//        Comment parentComment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
//
//        List<Comment> recomments = commentRepository.findByParentComment(parentComment);
//        return recomments.stream()
//                .map(CommentDto::fromEntity)
//                .toList();
//
//    }
//
//    public CommentDto editRecomment(Long recommentId, CommentDto commentDto) {
//        Comment recomment = commentRepository.findById(recommentId)
//                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다."));
//
//        recomment.edit(commentDto.getContent());
//        Comment updatedRecomment = commentRepository.save(recomment);
//
//        return CommentDto.fromEntity(updatedRecomment);
//    }
//
//    public void deleteRecomment(Long recommentId) {
//        Comment recomment = commentRepository.findById(recommentId)
//                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다."));
//
//        commentRepository.delete(recomment);
//    }
}
