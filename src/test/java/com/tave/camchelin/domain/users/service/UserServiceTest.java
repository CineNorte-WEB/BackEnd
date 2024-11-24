package com.tave.camchelin.domain.users.service;

import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.users.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
        // 테스트 후 트랜잭션 롤백
    void testRegisterUser() {
        String username = "testUser";
        String password = "password123";
        String nickname = "Test Nickname";
        Univ univ = Univ.builder()
                .name("Test University")
                .build();

        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .univ(univ)
                .build();

        UserDto savedUserDto = userService.registerUser(userDto);

        assertNotNull(savedUserDto.getId());  // 사용자 ID가 생성되었는지 확인
    }

    @Test
    @Transactional
    void testUpdateUser() {

        String username = "testUser";
        String password = "password123";
        String nickname = "Test Nickname";
        Univ univ = Univ.builder()
                .name("Test University")
                .build();;

        // 사용자 생성
        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .univ(univ)
                .build();

        // 변경할 내용 준비 (UserDto)
        String updatedNickname = "Updated Nickname";
        String updatedPassword = "UpdatedPassword123";
        UserDto updateDto = new UserDto(
                userDto.getId(),  // 기존 사용자 ID
                userDto.getUsername(),
                updatedPassword,
                updatedNickname,
                userDto.getUniv()
        );

        // 서비스 호출
        UserDto updatedUserDto = userService.updateUser(userDto);

        // 검증
        assertNotNull(updatedUserDto.getId());  // id가 반환되는지 확인

    }
}