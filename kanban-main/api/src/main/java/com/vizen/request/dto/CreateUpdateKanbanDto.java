package com.vizen.request.dto;

import io.swagger.annotations.ApiModel;
import jakarta.persistence.Column;
import lombok.experimental.Accessors;

@ApiModel(value = "KanbanCreateUpdateRequest", description = "Parameters required to create a tour")
@Accessors(chain = true)
public class CreateUpdateKanbanDto {
	private String id;
	private String title;
    private String statusId; 
    private boolean completed;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public boolean getCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
    
    
}
