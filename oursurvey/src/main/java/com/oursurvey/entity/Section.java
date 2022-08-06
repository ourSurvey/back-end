package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "section")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"survey"})
@DynamicInsert
@DynamicUpdate
public class Section extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "next_section", nullable = false)
    private Long nextSection=0L;

    @Builder
    public Section(Long id, Survey survey, String title, Long nextSection) {
        this.id = id;
        this.survey = survey;
        this.title = title;
        this.nextSection = nextSection;
    }
}
