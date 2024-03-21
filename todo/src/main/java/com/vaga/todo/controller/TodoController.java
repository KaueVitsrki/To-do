package com.vaga.todo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TodoService todoService;
    
    @PostMapping
    public ResponseEntity<List<TodoDto>> createTodo(@RequestBody @Valid TodoDto todoDto){
        List<TodoDto> create = todoService.createTodo(todoDto);
        return ResponseEntity.ok(create);
    }

    @PutMapping("/{idTodo}")
    public ResponseEntity<List<TodoDto>> updateTodo(@PathVariable UUID idTodo, @RequestBody @Valid TodoDto todoDto){
        List<TodoDto> update = todoService.updateTodo(idTodo, todoDto);
        return ResponseEntity.ok(update);
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> listTodoUser(){
        List<TodoDto> list = todoService.listTodoUser();
        return ResponseEntity.ok(list);
    }   

    @DeleteMapping("/{idTodo}")
    public ResponseEntity deleteTodoUser(@PathVariable UUID idTodo){
        todoService.deleteTodoUser(idTodo);
        return ResponseEntity.noContent().build();
    }
}
