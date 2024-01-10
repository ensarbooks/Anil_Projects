package com.vizen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vizen.entity.Kanban;

@Repository
public interface KanbanRepository extends JpaRepository<Kanban, String> {

	Kanban findByTitle(String title);

}
