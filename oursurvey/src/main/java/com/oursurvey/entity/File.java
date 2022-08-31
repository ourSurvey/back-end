package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;

@Entity(name = "file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
public class File extends CommonDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "table_pk")
    private Long tablePk;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "dir")
    private String dir;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "ext", nullable = false)
    private String ext;

    @Builder
    public File(Long id, User user, Long tablePk, String tableName, String originName, String dir, String name, String path, String ext) {
        this.id = id;
        this.user = user;
        this.tablePk = tablePk;
        this.tableName = tableName;
        this.originName = originName;
        this.dir = dir;
        this.name = name;
        this.path = path;
        this.ext = ext;
    }

    public void changeTablePkName(Long pk, String name) {
        this.tablePk = pk;
        this.tableName = name;
    }
}
