package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"grade"})
@DynamicInsert
@DynamicUpdate
public class User extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Enums gender;

    @Column(name = "age")
    private LocalDate age;

    @Column(name = "tel")
    private String tel;

    @Builder
    public User(Long id, Grade grade, String email, String nickname, String pwd, Enums gender, LocalDate age, String tel) {
        this.id = id;
        this.grade = grade;
        this.email = email;
        this.nickname = nickname;
        this.pwd = pwd;
        this.gender = gender;
        this.age = age;
        this.tel = tel;
    }
}
