package com.my.bob.core.domain.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedBy
    @Column(updatable = false)
    private String regId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime regDate;

    @LastModifiedBy
    @Column(nullable = false)
    private String modId;


    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modDate;
}
