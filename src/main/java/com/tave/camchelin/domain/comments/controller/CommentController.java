package com.tave.camchelin.domain.comments.controller;

import com.tave.camchelin.domain.comments.dto.CommentDto;
import com.tave.camchelin.domain.comments.service.CommentService;
import com.tave.camchelin.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{post_id}")
    public ResponseEntity<CommentDto> writeComment(
            @PathVariable("post_id") Long postId,
            @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.writeComment(postId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // 댓글 목록 조회
    @GetMapping("/{post_id}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable("post_id") Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentDto> editComment(@PathVariable("comment_id") Long commentId, @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.editComment(commentId, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
//
//    // 대댓글 작성
//    @PostMapping("/recomment/{comment_id}")
//    public ResponseEntity<CommentDto> writeRecomment(@PathVariable("comment_id") Long parentCommentId, @RequestBody CommentDto commentDto) {
//        CommentDto createdRecomment = commentService.writeRecomment(parentCommentId, commentDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecomment);
//    }
//
//    // 대댓글 목록 조회
//    @GetMapping("/recomment/{comment_id}")
//    public ResponseEntity<List<CommentDto>> getRecommentsByComment(@PathVariable("comment_id") Long commentId) {
//        List<CommentDto> recomments = commentService.getRecommentsByComment(commentId);
//        return ResponseEntity.ok(recomments);
//    }
//
//    // 대댓글 수정
//    @PutMapping("/recomment/{recomment_id}")
//    public ResponseEntity<CommentDto> editRecomment(@PathVariable("recomment_id") Long recommentId, @RequestBody CommentDto commentDto) {
//        CommentDto updatedRecomment = commentService.editRecomment(recommentId, commentDto);
//        return ResponseEntity.ok(updatedRecomment);
//    }
//
//    // 대댓글 삭제
//    @DeleteMapping("/recomment/{recomment_id}")
//    public ResponseEntity<Void> deleteRecomment(@PathVariable("recomment_id") Long recommentId) {
//        commentService.deleteRecomment(recommentId);
//        return ResponseEntity.ok().build();
//    }
}
