package com.vizen.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "user_password_reset_request")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPasswordResetRequest extends BaseEntity {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "expire_date_time")
    private Timestamp expireDateTime;
}
