package com.tave.camchelin.domain.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindPwResponseDto {

    private String receiveAddress;

    private String mailTitle;

    private String mailContent;

}
