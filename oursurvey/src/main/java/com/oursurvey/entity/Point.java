package com.oursurvey.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"user"})
@DynamicInsert
@DynamicUpdate
public class Point extends CommonDate {
    public final static Integer JOIN_VALUE = 1000;
    public final static String JOIN_REASON = "JOIN";

    public final static Integer LOGIN_VALUE = 50;
    public final static String LOGIN_REASON = "LOGIN";

    public final static Integer REPLY_SURVEY_VALUE = 300;
    public final static String REPLY_SURVEY_REASON = "REPLY_SURVEY";

    public final static Integer LEVEL_UP_VALUE = 1000;
    public final static String LEVEL_UP_REASON = "LEVEL_UP";

    public final static Integer VIEW_AD_VALUE = 80;
    public final static String VIEW_AD_REASON = "VIEW_AD";

    public final static Integer CREATE_SURVEY_VALUE = -500;
    public final static String CREATE_SURVEY_REASON = "CREATE_SURVEY";

    public final static Integer PULLING_SURVEY_VALUE = -200;
    public final static String PULLING_SURVEY_REASON = "PULLING_SURVEY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "table_pk")
    private String tablePk;

    @Column(name = "table_name")
    private String tableName="";

    @Builder
    public Point(Long id, User user, Integer value, String reason, String tablePk, String tableName) {
        this.id = id;
        this.user = user;
        this.value = value;
        this.reason = reason;
        this.tablePk = tablePk;
        this.tableName = tableName;
    }
}
