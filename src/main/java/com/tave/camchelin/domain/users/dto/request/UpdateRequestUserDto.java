package com.tave.camchelin.domain.users.dto.request;
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
public class UpdateRequestUserDto {
    private Long id;
    private String email;
    private String nickname;
    private Long univId;
    public User toEntity(Univ univ) {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .univ(univ)
                .build();
    }
    public static UpdateRequestUserDto fromEntity(User user) {
        return UpdateRequestUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .univId(user.getUniv() != null ? user.getUniv().getId() : null)
                .build();
    }
}