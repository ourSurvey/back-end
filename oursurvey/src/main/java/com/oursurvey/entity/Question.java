package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"section"})
@DynamicInsert
@DynamicUpdate
public class Question extends CommonDate implements Persistable<String> {
    public final static String NAME = "question";

    @Id
    @Column(name = "id")
    private String id;

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

    @Column(name = "random_show_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer randomShowFl;

    @Column(name = "next_fl", columnDefinition = "TINYINT(3)", nullable = false)
    private Integer nextFl;

    @Override
    public boolean isNew() {
        return createDt == null;
    }

    @Builder
    public Question(String id, Section section, String ask, String descrip, Integer oder, Integer multiFl, Integer duplFl, Integer essFl, Integer randomShowFl, Integer nextFl) {
        this.id = id;
        this.section = section;
        this.ask = ask;
        this.descrip = descrip;
        this.oder = oder;
        this.multiFl = multiFl;
        this.duplFl = duplFl;
        this.essFl = essFl;
        this.randomShowFl = randomShowFl;
        this.nextFl = nextFl;
    }
}
