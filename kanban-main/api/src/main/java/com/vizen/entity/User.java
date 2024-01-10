package com.vizen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    public enum Role  { ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_USER }

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "last_login_date_time")
    private Timestamp lastLoginDateTime;


    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;

}
