package com.tave.camchelin.domain.users.repository;

import com.tave.camchelin.domain.univs.repository.UnivRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.univs.entity.Univ;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "JWT_SECRET=dqR0ANkwWRbmPHifQqXir7bfG3SazQ2T")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Autowired
    UnivRepository univRepository;  // UnivRepository를 추가하여 Univ 엔티티 관리

    @AfterEach
    public void after() {
        em.clear();
    }

    @Test
    public void 회원저장_성공() throws Exception {
        // given
        Univ univ = Univ.builder()
                .name("세종대학교")
                .imageUrl("https://example.com/sejong.jpg")
                .address("서울특별시 세종로")
                .build();
        univRepository.save(univ); // Univ 저장

        User user = User.builder()
                .email("hy@naver.com")
                .password("1234567890")
                .nickname("NickName1")
                .univ(univ)  // Univ 객체 연관 설정
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("저장된 회원이 없습니다"));

        assertThat(foundUser).isSameAs(savedUser);
    }

    @Test
    public void 오류_회원가입시_이메일이_없음() throws Exception {
        // given
        Univ univ = Univ.builder()
                .name("세종대학교")
                .imageUrl("https://example.com/sejong.jpg")
                .address("서울특별시 세종로")
                .build();
        univRepository.save(univ); // Univ 저장

        User user = User.builder()
                .password("1234567890")
                .nickname("NickName1")
                .univ(univ)
                .build();

        // when, then
        assertThrows(Exception.class, () -> userRepository.save(user)); // 이메일이 없으면 예외 발생
    }

    @Test
    public void 오류_회원가입시_비밀번호가_없음() throws Exception {
        // given
        Univ univ = Univ.builder()
                .name("세종대학교")
                .imageUrl("https://example.com/sejong.jpg")
                .address("서울특별시 세종로")
                .build();
        univRepository.save(univ); // Univ 저장

        User user = User.builder()
                .email("hy@naver.com")
                .nickname("NickName1")
                .univ(univ)
                .build();

        // when, then
        assertThrows(Exception.class, () -> userRepository.save(user)); // 비밀번호가 없으면 예외 발생
    }

    @Test
    public void 오류_회원가입시_닉네임이_없음() throws Exception {
        // given
        Univ univ = Univ.builder()
                .name("세종대학교")
                .imageUrl("https://example.com/sejong.jpg")
                .address("서울특별시 세종로")
                .build();
        univRepository.save(univ); // Univ 저장

        User user = User.builder()
                .email("hy@naver.com")
                .password("1234567890")
                .univ(univ)
                .build();

        // when, then
        assertThrows(Exception.class, () -> userRepository.save(user)); // 닉네임이 없으면 예외 발생
    }

    @Test
    public void 오류_회원가입시_중복된_이메일이_있음() throws Exception {
        // given
        Univ univ = Univ.builder()
                .name("세종대학교")
                .imageUrl("https://example.com/sejong.jpg")
                .address("서울특별시 세종로")
                .build();
        univRepository.save(univ); // Univ 저장

        User user1 = User.builder()
                .email("hy@naver.com")
                .password("1234567890")
                .nickname("NickName1")
                .univ(univ)
                .build();
        userRepository.save(user1);
        em.clear();

        User user2 = User.builder()
                .email("hy@naver.com")  // 중복된 이메일
                .password("1111111111")
                .nickname("NickName2")
                .univ(univ)
                .build();

        // when, then
        assertThrows(Exception.class, () -> userRepository.save(user2)); // 중복된 이메일이면 예외 발생
    }
}
