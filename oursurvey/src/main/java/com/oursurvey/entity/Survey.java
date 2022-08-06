package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"user"})
@DynamicInsert
@DynamicUpdate
public class Survey extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "minute", nullable = false)
    private Integer minute;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "open_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer openFl;

    @Column(name = "closing_comment")
    private String closingComment;

    @Builder
    public Survey(Long id, User user, String subject, String content, Integer minute, LocalDate startDate, LocalDate endDate, Integer openFl, String closingComment) {
        this.id = id;
        this.user = user;
        this.subject = subject;
        this.content = getContent();
        this.minute = minute;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openFl = openFl;
        this.closingComment = closingComment;
    }
}
