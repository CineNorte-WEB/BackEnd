package com.tave.camchelin.domain.users.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestPwDto {
    private String currentPassword; // 현재 비밀번호
    private String newPassword; // 새 비밀번호
    private String confirmPassword; // 새 비밀번호 확인
}