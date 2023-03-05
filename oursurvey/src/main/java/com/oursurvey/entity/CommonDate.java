package com.oursurvey.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@ToString
public abstract class CommonDate {
    @CreatedDate
    @Column(name = "created_dt", updatable = false)
    LocalDateTime createDt;

    @LastModifiedDate
    @Column(name = "updated_dt")
    LocalDateTime updateDt;
}
