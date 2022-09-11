package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "pk_manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
public class PkManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "prefix", nullable = false)
    private String prefix;

    @Column(name = "yy", nullable = false)
    private Integer yy;

    @Column(name = "mm", nullable = false)
    private Integer mm;

    @Column(name = "dd", nullable = false)
    private Integer dd;

    @Column(name = "alpha", columnDefinition = "CHAR", nullable = false)
    private Character alpha;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Builder
    public PkManager(Long id, String tableName, String prefix, Integer yy, Integer mm, Integer dd, Character alpha, Integer count) {
        this.id = id;
        this.tableName = tableName;
        this.prefix = prefix;
        this.yy = yy;
        this.mm = mm;
        this.dd = dd;
        this.alpha = alpha;
        this.count = count;
    }

    public void update(Integer yy, Integer mm, Integer dd, Character alpha, Integer count) {
        this.yy = yy;
        this.mm = mm;
        this.dd = dd;
        this.alpha = alpha;
        this.count = count;
    }

    public void update(Character alpha, Integer count) {
        this.alpha = alpha;
        this.count = count;
    }

    public void update(Integer count) {
        this.count = count;
    }
}
