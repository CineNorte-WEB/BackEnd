package com.tave.camchelin.domain.users.service;

import com.tave.camchelin.domain.board_posts.dto.response.ResponseBoardDto;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.bookmarks.entity.Bookmark;
import com.tave.camchelin.domain.bookmarks.repository.BookmarkRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import com.tave.camchelin.domain.review_posts.dto.response.ResponseReviewDto;
import com.tave.camchelin.domain.review_posts.repository.ReviewPostRepository;
import com.tave.camchelin.domain.univs.entity.Univ;
import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.dto.UserDto;
import com.tave.camchelin.domain.users.dto.request.FindPwRequestDto;
import com.tave.camchelin.domain.users.dto.request.UpdateRequestUserDto;
import com.tave.camchelin.domain.users.dto.response.FindPwResponseDto;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import com.tave.camchelin.global.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UnivRepository univRepository;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BoardPostRepository boardPostRepository;
    private final ReviewPostRepository reviewPostRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailSender mailSender;


    @Transactional
    public UserDto registerUser(UserDto userDto) {
        // 비즈니스 로직 구현
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        };

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        Univ univ = null;

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .email(userDto.getEmail())
                .password(encodedPassword) // 암호화된 비밀번호 사용
                .nickname(userDto.getNickname())
                .univ(univ)
                .build();

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
    public UserDto updateUser(UpdateRequestUserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다." ));

        Univ univ = null;
        if (userDto.getUnivId() != null) {
            univ = univRepository.findById(userDto.getUnivId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다."));
        }

        user.update(
                userDto.getEmail(),
                userDto.getNickname(),
                univ
        );

        User updatedUser = userRepository.save(user);

        return UserDto.fromEntity(updatedUser);
    }

    @Transactional
    public void deleteUser(Long userId, String token) {
        // 1. 사용자 삭제
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        userRepository.deleteById(userId);
        log.info("사용자 ID {} 삭제 완료", userId);

        // 2. 블랙리스트 등록 (JwtService 호출)
        if (token != null && !token.isEmpty()) {
            try {
                jwtService.addTokenToBlacklist(token);
            } catch (Exception e) {
                log.error("토큰 블랙리스트 추가 중 오류 발생: {}", e.getMessage());
            }
        }
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
    public Page<ResponseBoardDto> getUserBoardPosts(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다."));

        return boardPostRepository.findByUserId(userId, pageable)
                .map(ResponseBoardDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ResponseReviewDto> getUserReviewPosts(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾지 못했습니다."));

        return reviewPostRepository.findByUserId(userId, pageable)
                .map(ResponseReviewDto::fromEntity);
    }



    @Transactional(readOnly = true)
    public Long extractUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없거나 올바르지 않은 형식입니다.");
        }

        String jwtToken = token.substring(7); // "Bearer " 제거

        // JwtService를 사용해 이메일 추출
        String email = jwtService.extractEmail(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        // 이메일로 사용자 ID 조회
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. 이메일: " + email));
    }


    @Transactional
    public String findPw(FindPwRequestDto request) throws Exception {
        // 이메일 검증
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new BadCredentialsException("유효하지 않은 이메일입니다."));

        // 임시비밀번호 발급
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*' };

        StringBuilder tempPw = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            tempPw.append(charSet[idx]);
        }

        // set findPwResponseDto
        FindPwResponseDto newDto = FindPwResponseDto.builder()
                .receiveAddress(request.getEmail())
                .mailTitle("임시 비밀번호 발급")
                .mailContent("메일 발송 테스트 중입니다.\n" +
                        "임시 비밀번호는 " + tempPw + " 입니다.")
                .build();

        // send e-mail
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("tjdgud3488@gmail.com");
        message.setTo(newDto.getReceiveAddress());
        message.setReplyTo("tjdgus3488@gmail.com");
        message.setSubject(newDto.getMailTitle());
        message.setText(newDto.getMailContent());

        mailSender.send(message);

        // 임시비밀번호로 변경
        user.updatePassword(passwordEncoder.encode(tempPw));
        userRepository.save(user);

        return "임시비밀번호 발급 완료";
    }
}

