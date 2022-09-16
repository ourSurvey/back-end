package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "top_survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"survey"})
@DynamicInsert
@DynamicUpdate
public class TopSurvey extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Builder
    public TopSurvey(Long id, Survey survey) {
        this.id = id;
        this.survey = survey;
    }
}
