package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "grade")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
public class Grade extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pivot", nullable = false)
    private Integer pivot;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public Grade(Long id, Integer pivot, String name) {
        this.id = id;
        this.pivot = pivot;
        this.name = name;
    }
}
