package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity(name = "question_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"question"})
@DynamicInsert
@DynamicUpdate
public class QuestionItem extends CommonDate implements Persistable<String> {
    public final static String NAME = "question_item";

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "oder", nullable = false)
    private Integer oder;

    @Column(name = "next_section")
    private Long nextSection;

    @Override
    public boolean isNew() {
        return createDt == null;
    }

    @Builder
    public QuestionItem(String id, Question question, String content, Integer oder, Long nextSection) {
        this.id = id;
        this.question = question;
        this.content = content;
        this.oder = oder;
        this.nextSection = nextSection;
    }
}
