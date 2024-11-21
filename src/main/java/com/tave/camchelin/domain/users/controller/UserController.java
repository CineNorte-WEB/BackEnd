package com.tave.camchelin.domain.users.controller;

import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto responseDto = userService.registerUser(userDto);
        return ResponseEntity.ok(responseDto);
    }


    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto responseDto = userService.updateUser(userDto);
        return ResponseEntity.ok(responseDto);
    }


}
