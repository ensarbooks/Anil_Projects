package com.vizen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "kanban_task")
@Data
@EqualsAndHashCode(callSuper = true)
public class Kanban extends BaseEntity {
	@Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "status_id", nullable = false, length = 255)
    private String statusId;

    @Column(name = "completed", nullable = false) 
    private boolean completed;

}
