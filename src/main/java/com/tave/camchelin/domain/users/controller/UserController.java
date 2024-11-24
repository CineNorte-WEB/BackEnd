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
        UserDto responseDto = userService.registerUser(userDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/profile") // 회원 정보 조회
    public ResponseEntity<UserDto> getUserProfile(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDto responseDto = userService.getUserProfile(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update") // 회원 정보 수정
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto responseDto = userService.updateUser(userDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{userId}/bookmarks/{placeId}")
    public ResponseEntity<Void> addBookmark(@PathVariable Long userId, @PathVariable Long placeId) {
        userService.addBookmark(userId, placeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/bookmarks/{placeId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long userId, @PathVariable Long placeId) {
        userService.removeBookmark(userId, placeId);
        return ResponseEntity.ok().build();
    }
}
