package com.tave.camchelin.domain.users.controller;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.dto.response.ResponseBoardDto;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.dto.response.ResponseReviewDto;
import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.dto.request.UpdateRequestUserDto;
import com.tave.camchelin.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register") // 회원 등록
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto registeredUserDto = userService.registerUser(userDto);
        return ResponseEntity.ok(registeredUserDto);
    }

    @GetMapping("/profile") // 회원 정보 조회
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String token) {
        Long userId = userService.extractUserIdFromToken(token);
        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto userDto = userService.getUserProfile(userId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update") // 회원 정보 수정
    public ResponseEntity<UserDto> updateUser(@RequestHeader("Authorization") String token,
                                              @RequestBody UpdateRequestUserDto userDto) {
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userDto.setId(userId); // 수정할 사용자의 ID를 UserDto에 설정
        UserDto updatedUserDto = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/delete") // 현재 로그인 사용자의 회원탈퇴
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token) {
        // 토큰에서 Bearer 제거 및 userId 추출
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // UserService에서 유저 삭제와 토큰 블랙리스트 처리
        userService.deleteUser(userId, token.substring(7)); // Bearer 제거 후 토큰 전달
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarks") // 현재 로그인 사용자의 북마크 조회
    public ResponseEntity<List<PlaceDto>> getBookmarks(@RequestHeader("Authorization") String token) {
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<PlaceDto> bookmarks = userService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }


    @GetMapping("/{userId}/bookmarks") // 특정 사용자의 북마크 조회
    public ResponseEntity<List<PlaceDto>> getUserBookmarks(@PathVariable Long userId) {
        List<PlaceDto> bookmarks = userService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @PostMapping("/bookmarks/{placeId}") // 현재 로그인 사용자의 북마크 추가
    public ResponseEntity<Void> addBookmark(@RequestHeader("Authorization") String token,
                                            @PathVariable Long placeId) {
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.addBookmark(userId, placeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bookmarks/{placeId}") // 현재 로그인 사용자의 북마크 삭제
    public ResponseEntity<Void> removeBookmark(@RequestHeader("Authorization") String token,
                                               @PathVariable Long placeId) {
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.removeBookmark(userId, placeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/boards") // 현재 로그인 사용자의 게시글 조회
    public ResponseEntity<List<ResponseBoardDto>> getUserBoards(@RequestHeader("Authorization") String token) {
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ResponseBoardDto> boardPosts = userService.getUserBoardPosts(userId);

        if (boardPosts.isEmpty()) {
            log.info("사용자 {}의 게시글이 없습니다.", userId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(boardPosts);
    }

    // 사용자가 작성한 리뷰 목록 조회
    @GetMapping("/reviews") // 현재 로그인 사용자의 리뷰 조회
    public ResponseEntity<List<ResponseReviewDto>> getUserReviews(@RequestHeader("Authorization") String token) {
        Long userId = userService.extractUserIdFromToken(token);

        if (userId == null) {
            log.error("유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ResponseReviewDto> reviewPosts = userService.getUserReviewPosts(userId);

        if (reviewPosts.isEmpty()) {
            log.info("사용자 {}의 리뷰가 없습니다.", userId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reviewPosts);
    }

}
