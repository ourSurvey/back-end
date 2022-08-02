package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"question"})
@DynamicInsert
@DynamicUpdate
public class QuestionItem extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "next_section")
    private Long nextSection;

    @Builder
    public QuestionItem(Question question, String content, Long nextSection) {
        this.question = question;
        this.content = content;
        this.nextSection = nextSection;
    }
}
