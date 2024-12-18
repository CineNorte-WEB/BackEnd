package com.tave.camchelin.domain.users.service;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.bookmarks.entity.Bookmark;
import com.tave.camchelin.domain.bookmarks.repository.BookmarkRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_posts.dto.ReviewPostDto;
import com.tave.camchelin.domain.review_posts.repository.ReviewPostRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UnivRepository univRepository;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BoardPostRepository boardPostRepository;
    private final ReviewPostRepository reviewPostRepository;

    @Transactional
    public UserDto registerUser(UserDto userDto) {
        // 비즈니스 로직 구현
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        };

        Univ univ = univRepository.findById(userDto.getUnivId())
                .orElseThrow(() -> new IllegalArgumentException("대학 정보를 찾을 수 없습니다."));


        User user = userDto.toEntity(univ);
        User savedUser = userRepository.save(user);

        return UserDto.fromEntity(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다."));
        return UserDto.fromEntity(user);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다." ));
        Univ univ = univRepository.findById(userDto.getUnivId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다."));


        user.update(
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getNickname(),
                univ
        );

        User updatedUser = userRepository.save(user);

        return UserDto.fromEntity(updatedUser);
    }

    @Transactional(readOnly = true)
    public List<PlaceDto> getUserBookmarks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다."));
        return bookmarkRepository.findByUserId(userId)
                .stream()
                .map(bookmark -> PlaceDto.fromEntity(bookmark.getPlace()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다."));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .place(place)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("맛집 정보를 찾지 못했습니다."));

        // 북마크 찾기
        Bookmark bookmark = bookmarkRepository.findByUserAndPlace(user, place)
                .orElseThrow(() -> new IllegalArgumentException("사용자와 맛집에 관한 정보를 찾지 못했습니다."));

        // 북마크 삭제
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BoardPostDto> getUserBoardPosts(Long userId) {
        User user = userRepository.findById(userId)  // 사용자 ID를 전달 받거나 세션에서 가져와야 할 수 있음
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        return boardPostRepository.findByUserId(user.getId())
                .stream()
                .map(BoardPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewPostDto> getUserReviewPosts(Long userId) {
        User user = userRepository.findById(userId)  // 사용자 ID를 전달 받거나 세션에서 가져와야 할 수 있음
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        return reviewPostRepository.findByUserId(user.getId())
                .stream()
                .map(ReviewPostDto::fromEntity)
                .collect(Collectors.toList());
    }

}

