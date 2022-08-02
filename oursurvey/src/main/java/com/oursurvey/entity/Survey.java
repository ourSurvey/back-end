package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"user"})
@DynamicInsert
@DynamicUpdate
public class Survey extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "minute", nullable = false)
    private Integer minute;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "open_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer openFl;

    @Column(name = "closing_comment")
    private String closingComment;

    @Builder
    public Survey(User user, String subject, Integer minute, LocalDateTime startDate, LocalDateTime endDate, Integer openFl, String closingComment) {
        this.user = user;
        this.subject = subject;
        this.minute = minute;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openFl = openFl;
        this.closingComment = closingComment;
    }
}
