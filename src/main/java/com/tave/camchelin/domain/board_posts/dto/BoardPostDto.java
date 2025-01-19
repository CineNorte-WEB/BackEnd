package com.tave.camchelin.domain.board_posts.dto;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostDto {
    private Long id;
    private User user;
    private Community community;
    private String title;
    private String content;

    public BoardPost toEntity(User user, Community community) {
        return BoardPost.builder()
                .user(user)
                .community(community)
                .title(this.title)
                .content(this.content)
                .build();
    }

    public static BoardPostDto fromEntity(BoardPost boardPost) {
        return BoardPostDto.builder()
                .id(boardPost.getId())
                .user(boardPost.getUser())
                .title(boardPost.getTitle())
                .content(boardPost.getContent())
                .build();
    }
}
