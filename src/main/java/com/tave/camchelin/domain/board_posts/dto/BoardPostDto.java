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
    private String title;
    private String content;
    private User user;
    private Community community;

    public BoardPost toEntity(User user, Community community) {
        return BoardPost.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .community(community)
                .build();
    }

    public static BoardPostDto fromEntity(BoardPost boardPost) {
        return BoardPostDto.builder()
                .id(boardPost.getId())
                .title(boardPost.getTitle())
                .content(boardPost.getContent())
                .user(boardPost.getUser())
                .community(boardPost.getCommunity())
                .build();
    }
}
