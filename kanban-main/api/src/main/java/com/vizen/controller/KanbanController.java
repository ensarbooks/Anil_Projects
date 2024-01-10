package com.vizen.controller;

import com.vizen.entity.Kanban;
import com.vizen.request.dto.CreateUpdateKanbanDto;
import com.vizen.service.KanbanService;

import io.swagger.annotations.Api;
import jakarta.validation.Valid;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "kanban Mgmt")
@RestController
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/kanban")
public class KanbanController {
	private KanbanService kanbanService;

    @Autowired
    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService; 
    }   
    
    @GetMapping("/current")
    public ResponseEntity<Kanban> getLoggedInKanban() {
        return ResponseEntity.ok(kanbanService.getLoggedInKanban());
    }
    
    // get all kanban items
    
    @GetMapping("/list")
    public ResponseEntity<Map<String, List<Kanban>>> getUsers(
            @RequestParam(name = "orgId", required = false) final String orgId
        ) {
        	
        	Map<String, List<Kanban>> result = new HashMap<>();
        	result.put("kanban", kanbanService.getAllKanban());
            return ResponseEntity.ok(result);
            
        }
    
    // create kanban item
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
	public ResponseEntity<Kanban> createKanaban(@Valid @RequestBody Kanban createUpdateKanbanDto) {
		try {
			
			return ResponseEntity.ok(kanbanService.createKanaban(createUpdateKanbanDto));
		} catch (Exception e) {
			// If any exception occurs during the leave creation, return an error response
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
		}
	}
    
    // edit kanban item
    
    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
	public ResponseEntity<Kanban> replaceKanban(@RequestBody CreateUpdateKanbanDto updatedKanban, @PathVariable String id) {
		
		return ResponseEntity.ok(kanbanService.updateKanban(id, updatedKanban));

	}
    
    // delete kanban item
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        String message = kanbanService.deleteById(id);
        return ResponseEntity.ok(message);
    }
}
