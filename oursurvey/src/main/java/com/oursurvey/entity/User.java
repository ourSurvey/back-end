package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"grade"})
@DynamicInsert
@DynamicUpdate
public class User extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "pwd", nullable = false)
    private String pwd;

    @Column(name = "gender", columnDefinition = "ENUM")
    @Enumerated(EnumType.STRING)
    private Enums gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "tel")
    private String tel;

    @Builder
    public User(Long id, Grade grade, String email, String nickname, String pwd, Enums gender, Integer age, String tel) {
        this.id = id;
        this.grade = grade;
        this.email = email;
        this.nickname = nickname;
        this.pwd = pwd;
        this.gender = gender;
        this.age = age;
        this.tel = tel;
    }

    // 비밀번호변경
    public void updatePwd(String pwd) {
        if (pwd != null) {
            this.pwd = pwd;
        }
    }

    // 레벨업
    public void promoteGrade(Grade grade) {
        this.grade = grade;
    }

    // 부가정보변경
    public void updateAddition(String gender, Integer age, String tel) {
        if (StringUtils.hasText(gender)) {
            this.gender = Enums.valueOf(gender);
        }

        if (age != null) {
            this.age = age;
        }

        if (StringUtils.hasText(tel)) {
            this.tel = tel;
        }
    }
}
