package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"user"})
@DynamicInsert
@DynamicUpdate
public class Survey extends CommonDate implements Persistable<String> {
    public final static String NAME = "survey";

    @Id
    @Column(name = "id")
    private String id;

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

    @Column(name = "temp_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer tempFl;

    @Column(name = "closing_comment")
    private String closingComment;

    @Column(name = "pull_date")
    private LocalDateTime pullDate;

    @Column(name = "view_cnt", nullable = false)
    private Integer viewCnt;

    @Override
    public boolean isNew() {
        return createDt == null;
    }

    @Builder
    public Survey(String id, User user, String subject, String content, Integer minute, LocalDate startDate, LocalDate endDate, Integer openFl, Integer tempFl, String closingComment, LocalDateTime pullDate, Integer viewCnt) {
        this.id = id;
        this.user = user;
        this.subject = subject;
        this.content = content;
        this.minute = minute;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openFl = openFl;
        this.tempFl = tempFl;
        this.closingComment = closingComment;
        this.pullDate = pullDate;
        this.viewCnt = viewCnt;
    }

    public void pullUp() {
        this.pullDate = LocalDateTime.now();
    }

    public void addViewCnt() {
        this.viewCnt++;
    }
}
