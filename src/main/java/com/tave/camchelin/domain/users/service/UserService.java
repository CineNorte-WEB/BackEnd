package com.tave.camchelin.domain.users.service;

import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto registerUser(UserDto userDto) {
        // 비즈니스 로직 구현
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        };

        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);

        return UserDto.fromEntity(savedUser);
    }

    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다.: " + userDto.getId()));

        user.update(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getNickname(),
                userDto.getSchool()
        );

        User updatedUser = userRepository.save(user);

        return UserDto.fromEntity(updatedUser);
    }
}

