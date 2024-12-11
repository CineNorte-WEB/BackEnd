package com.tave.camchelin.domain.comments.dto;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.comments.entity.Comment;
import com.tave.camchelin.domain.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private BoardPost boardPost;
    private String content;
    private User user;
    private Comment parentComment;

    public Comment toEntity() {
        return Comment.builder()
                .boardPost(this.boardPost)
                .content(this.content)
                .user(this.user)
                .parentComment(this.parentComment)
                .build();
    }

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .boardPost(comment.getBoardPost())
                .content(comment.getContent())
                .user(comment.getUser())
                .parentComment(comment.getParentComment())
                .build();
    }
}
