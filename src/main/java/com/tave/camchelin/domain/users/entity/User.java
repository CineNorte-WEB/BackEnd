package com.tave.camchelin.domain.users.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.bookmarks.entity.Bookmark;
import com.tave.camchelin.domain.review_posts.entity.ReviewPost;
import com.tave.camchelin.domain.univs.entity.Univ;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "univ_id")
    private Univ univ;

    @Column(nullable = false, unique = true) // NOT NULL 및 UNIQUE 제약 조건
    private String email;

    @Column(nullable = false) // NOT NULL
    private String password;

    @Column(nullable = false) // NOT NULL
    private String nickname;


    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewPost> reviewPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewPost> boardPosts = new ArrayList<>();

    public void update(String email, String nickname, Univ univ) {
        this.email = email;
        this.nickname = nickname;
        this.univ= univ;
    }

    //jwt 토큰 추가
    @Column(length = 1000)
    private String refreshToken;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public void updatePassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 비어 있을 수 없습니다.");
        }
        this.password = encodedPassword;
    }
}