package com.vaga.todo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.service.TodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @PostMapping
    public ResponseEntity<List<TodoDto>> create(@RequestBody @Valid TodoDto todoDto){
        List<TodoDto> create = todoService.create(todoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping
    public ResponseEntity<List<TodoDto>> update(@RequestBody @Valid TodoDto todoDto){
        List<TodoDto> update = todoService.update(todoDto);
        return ResponseEntity.ok(update);
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> list(){
        List<TodoDto> list = todoService.list();
        return ResponseEntity.ok(list);
    }   

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Valid UUID id){
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
