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
}