package com.tave.camchelin.domain.board_posts.dto.request;

import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestBoardDto {
    private String title; // 수정할 제목
    private String content; // 수정할 내용

    public BoardPost toEntity(User user) {
        return BoardPost.builder()
                .user(user)
                .title(this.title)
                .content(this.content)
                .build();
    }

    public static UpdateRequestBoardDto fromEntity(BoardPost boardPost) {
        return UpdateRequestBoardDto.builder()
                .title(boardPost.getTitle())
                .content(boardPost.getContent())
                .build();
    }
}
