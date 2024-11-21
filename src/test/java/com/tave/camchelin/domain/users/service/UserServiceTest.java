package com.tave.camchelin.domain.users.service;

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
        String school = "Test School";

        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .school(school)
                .build();

        UserDto savedUserDto = userService.registerUser(userDto);

        assertNotNull(savedUserDto.getId());  // 사용자 ID가 생성되었는지 확인
    }

    @Test
    @Transactional
    void testUpdateUser() {

    }
}