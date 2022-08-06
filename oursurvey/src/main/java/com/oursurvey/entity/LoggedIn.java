package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "logged_in")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"user"})
@DynamicInsert
@DynamicUpdate
public class LoggedIn extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "remote_addr", nullable = false)
    private String remoteAddr;

    @Builder
    public LoggedIn(Long id, User user, String remoteAddr) {
        this.id = id;
        this.user = user;
        this.remoteAddr = remoteAddr;
    }
}
