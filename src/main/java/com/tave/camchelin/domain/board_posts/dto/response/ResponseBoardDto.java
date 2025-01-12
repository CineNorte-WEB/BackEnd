package com.tave.camchelin.domain.board_posts.dto.response;

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
public class ResponseBoardDto {

    private Long id;
    private String title;
    private String content;
    private String userNickname;
    // Entity -> DTO 변환 메서드
    public static ResponseBoardDto fromEntity(BoardPost boardPost) {
        User user = boardPost.getUser();

        return ResponseBoardDto.builder()
                .id(boardPost.getId())
                .title(boardPost.getTitle())
                .content(boardPost.getContent())
                .userNickname(user != null ? user.getNickname() : null)
                .build();
    }
}
