package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"section"})
@DynamicInsert
@DynamicUpdate
public class Question extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "ask", nullable = false)
    private String ask;

    @Column(name = "descrip")
    private String descrip;

    @Column(name = "oder", nullable = false)
    private Integer oder;

    @Column(name = "multi_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer multiFl;

    @Column(name = "dupl_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer duplFl;

    @Column(name = "ess_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer essFl;

    @Builder
    public Question(Long id, Section section, String ask, String descrip, Integer oder, Integer multiFl, Integer duplFl, Integer essFl) {
        this.id = id;
        this.section = section;
        this.ask = ask;
        this.descrip = descrip;
        this.oder = oder;
        this.multiFl = multiFl;
        this.duplFl = duplFl;
        this.essFl = essFl;
    }
}
