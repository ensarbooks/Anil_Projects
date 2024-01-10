package com.vizen.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    private String id;

    @CreatedDate
    @Column(name = "created_date_time")
    private Timestamp createdDateTime;

    @LastModifiedDate
    @Column(name = "last_updated_date_time")
    private Timestamp lastUpdatedDateTime;

    @PrePersist
    public void init() {
        id = UUID.randomUUID().toString();
        createdDateTime = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdatedDateTime = Timestamp.valueOf(LocalDateTime.now());
    }

}
