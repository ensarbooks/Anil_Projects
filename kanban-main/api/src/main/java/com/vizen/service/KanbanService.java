package com.vizen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vizen.entity.Kanban;
import com.vizen.repository.KanbanRepository;
import com.vizen.request.dto.CreateUpdateKanbanDto;

import java.util.List;
import java.util.Optional;


@Service
public class KanbanService {
	private final KanbanRepository kanbanRepository;

    @Autowired
    public KanbanService(KanbanRepository kanbanRepository) {
        this.kanbanRepository = kanbanRepository;
    }
    
    public Kanban getLoggedInKanban() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByTitle(authentication.getName());
	}
    
    private Kanban getUserByTitle(String title) {
		return kanbanRepository.findByTitle(title);

	}
    
 // this is for get all kanban items
 
	public List<Kanban> getAllKanban() {
        return kanbanRepository.findAll();
    }
    
// this is the delete service 
    
    public String deleteById(String id) {
        Optional<Kanban> tourOptional = kanbanRepository.findById(id);

        if (!tourOptional.isPresent()) {
            throw new RuntimeException("Tour with id " + id + " not found.");
        }

        kanbanRepository.deleteById(id);
        return "Tour Item deleted successfully.";
    }


	
	public Kanban createKanaban(Kanban kanbanResponse) {
	      return kanbanRepository.save(kanbanResponse);
	  }

	  public Kanban updateKanban(String id, CreateUpdateKanbanDto updatedKanban) {
	      Optional<Kanban> kanban = kanbanRepository.findById(id);

	      if (!kanban.isPresent()) {
	          throw new RuntimeException("kanban with id " + id + " not found.");
	      }

	      Kanban existingKaban= kanban.get();
	      
	      existingKaban.setTitle(updatedKanban.getTitle());
	      existingKaban.setStatusId(updatedKanban.getStatusId()); 
	      existingKaban.setCompleted(updatedKanban.getCompleted());

	      Kanban updateKanban = kanbanRepository.save(existingKaban);
	      return updateKanban;
	  }


}