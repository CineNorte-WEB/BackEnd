package com.tave.camchelin.domain.board_posts.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBoardDto {

    private Long id;
    private String title;
    private String content;
    private String userNickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 생성 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt; // 수정 시간
    // Entity -> DTO 변환 메서드
    public static ResponseBoardDto fromEntity(BoardPost boardPost) {
        User user = boardPost.getUser();

        return ResponseBoardDto.builder()
                .id(boardPost.getId())
                .title(boardPost.getTitle())
                .content(boardPost.getContent())
                .userNickname(user != null ? user.getNickname() : null)
                .createdAt(boardPost.getCreatedAt())
                .updatedAt(boardPost.getUpdatedAt())
                .build();
    }
}
