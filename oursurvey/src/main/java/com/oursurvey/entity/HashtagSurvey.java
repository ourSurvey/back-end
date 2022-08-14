package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "hashtag_survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@IdClass(HashtagSurveyIdClass.class)
public class HashtagSurvey implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", referencedColumnName = "id", nullable = false)
    private Survey survey;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", referencedColumnName = "id", nullable = false)
    private Hashtag hashtag;

    @Builder
    public HashtagSurvey(Survey survey, Hashtag hashtag) {
        this.survey = survey;
        this.hashtag = hashtag;
    }
}
