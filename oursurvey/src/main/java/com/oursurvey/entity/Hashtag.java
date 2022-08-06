package com.oursurvey.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value", unique = true, nullable = false)
    private String value;
}
