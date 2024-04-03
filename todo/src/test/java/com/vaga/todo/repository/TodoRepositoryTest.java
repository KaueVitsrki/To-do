package com.vaga.todo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.model.TodoModel;
import com.vaga.todo.service.TodoService;

@ExtendWith(MockitoExtension.class)
class TodoRepositoryTest {
    @InjectMocks
    private TodoService todoService;
    
    @Mock
    private TodoRepository todoRepository;

    @Test
    void existsByNameSuccess(){
        UUID id = UUID.fromString("3864f1c7-0a60-4a6e-a4b4-df8a3748ed61");
        TodoDto todoInput = new TodoDto(id, "seila", "Tururu", false, 2);
        TodoModel todo = this.createTodo(todoInput);

        when(todoRepository.existsByName(todo.getName())).thenReturn(true);

        boolean existsByNameSuccess = todoRepository.existsByName(todo.getName());

        assertEquals(true, existsByNameSuccess);
    }

    @Test
    void existsByNameError(){
        UUID id = UUID.fromString("3864f1c7-0a60-4a6e-a4b4-df8a3748ed61");
        TodoDto todoInput = new TodoDto(id, "seila", "Tururu", false, 2);
        TodoModel todo = this.createTodo(todoInput);

        when(todoRepository.existsByName(todo.getName())).thenReturn(false);

        boolean existsByNameSuccess = todoRepository.existsByName(todo.getName());

        assertEquals(false, existsByNameSuccess);
    }
        
    private TodoModel createTodo(TodoDto todoDto) {
        TodoModel todoModel = new TodoModel();
        todoModel.setId(todoDto.getId());
        todoModel.setName(todoDto.getName());
        todoModel.setDescription(todoDto.getDescription());
        todoModel.setAccomplished(todoDto.isAccomplished());
        todoModel.setPriority(todoDto.getPriority());
        return todoModel;
    }
}
