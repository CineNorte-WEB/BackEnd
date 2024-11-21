package com.tave.camchelin.domain.users.entity;

import com.tave.camchelin.domain.BaseEntity;
import com.tave.camchelin.domain.users.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true) // NOT NULL 및 UNIQUE 제약 조건
    private String username;

    @Column(nullable = false) // NOT NULL
    private String password;

    @Column(nullable = false) // NOT NULL
    private String nickname;

    @Column(nullable = false) // NOT NULL
    private String school;

    public void update(String username, String password, String nickname, String school) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.school= school;
    }
}
