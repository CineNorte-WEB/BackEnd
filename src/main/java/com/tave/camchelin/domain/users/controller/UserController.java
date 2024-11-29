package com.tave.camchelin.domain.users.controller;

import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register") // 회원 등록
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUserDto = userService.registerUser(userDto);
        return ResponseEntity.ok(registeredUserDto);
    }

    @GetMapping("/profile") // 회원 정보 조회
    public ResponseEntity<UserDto> getUserProfile(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto userDto = userService.getUserProfile(userId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update") // 회원 정보 수정
    public ResponseEntity<UserDto> updateUser(HttpServletRequest request, @RequestBody UserDto userDto) {
        Long userId = (Long) request.getSession().getAttribute("userId"); // 세션에서 userId 추출
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userDto.setId(userId); // 수정할 사용자의 ID를 UserDto에 설정
        UserDto updatedUserDto = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<List<PlaceDto>> getUserBookmarks(@PathVariable Long userId) {
        List<PlaceDto> bookmarks = userService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @PostMapping("/{userId}/bookmarks/{placeId}") // 찜 추가
    public ResponseEntity<Void> addBookmark(@PathVariable Long userId, @PathVariable Long placeId) {
        userService.addBookmark(userId, placeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/bookmarks/{placeId}") // 찜 삭제
    public ResponseEntity<Void> removeBookmark(@PathVariable Long userId, @PathVariable Long placeId) {
        userService.removeBookmark(userId, placeId);
        return ResponseEntity.ok().build();
    }
}
