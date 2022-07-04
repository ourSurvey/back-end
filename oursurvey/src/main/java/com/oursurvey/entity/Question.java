package com.oursurvey.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"section"})
@DynamicInsert
@DynamicUpdate
public class Question extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "ask", nullable = false)
    private String ask;

    @Column(name = "oder", nullable = false)
    private Integer oder;

    @Column(name = "multi_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer multiFl;

    @Column(name = "dupl_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer duplFl;

    @Column(name = "ess_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer essFl;

    @Column(name = "explain")
    private String explain;
}
