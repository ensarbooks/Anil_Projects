package com.vizen.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity(name = "forecast_dash_board")
@Data
@EqualsAndHashCode(callSuper = true)
public class ForecastDashBoard extends BaseEntity {

    @Column(name = "dash_board_id")
    private String dashBoardId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;

    @Transient
    private String url;

}
