package com.oursurvey.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "hashtag_survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@IdClass(HashtagSurvey.class)
public class HashtagSurvey implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;
}
