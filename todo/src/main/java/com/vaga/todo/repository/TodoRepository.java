package com.vaga.todo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaga.todo.model.TodoModel;

public interface TodoRepository extends JpaRepository<TodoModel, UUID>{
    
}
