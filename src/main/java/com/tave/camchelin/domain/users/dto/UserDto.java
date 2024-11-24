package com.tave.camchelin.domain.users.dto;

import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Univ univ;

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .univ(this.univ)
                .build();
    }

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .univ(user.getUniv())
                .build();
    }
}
