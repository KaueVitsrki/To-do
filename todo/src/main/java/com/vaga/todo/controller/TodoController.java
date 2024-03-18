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
    
    @PostMapping("/users/{idUser}")
    public ResponseEntity<List<TodoDto>> createTodo(@PathVariable UUID idUser, @RequestBody @Valid TodoDto todoDto){
        List<TodoDto> create = todoService.createTodo(idUser, todoDto);
        return ResponseEntity.ok(create);
    }

    @PutMapping("/users/{idUser}/todos/{idTodo}")
    public ResponseEntity<List<TodoDto>> updateTodo(@PathVariable UUID idUser, @PathVariable UUID idTodo, @RequestBody @Valid TodoDto todoDto){
        List<TodoDto> update = todoService.updateTodo(idUser, idTodo, todoDto);
        return ResponseEntity.ok(update);
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<List<TodoDto>> listTodoUser(@PathVariable UUID idUser){
        List<TodoDto> list = todoService.listTodoUser(idUser);
        return ResponseEntity.ok(list);
    }   

    @DeleteMapping("/users/{idUser}/todos/{idTodo}")
    public ResponseEntity deleteTodoUser(@PathVariable UUID idUser, @PathVariable UUID idTodo){
        todoService.deleteTodoUser(idUser, idTodo);
        return ResponseEntity.noContent().build();
    }
}
