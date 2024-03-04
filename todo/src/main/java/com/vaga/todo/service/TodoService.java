package com.vaga.todo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.model.TodoModel;
import com.vaga.todo.repository.TodoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TodoService {
    private TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public List<TodoDto> create(TodoDto todoDto){
        todoRepository.save(convertDtoEntity(todoDto));
        return list();
    }

    public List<TodoDto> list(){
        Sort sort = Sort.by("priority").descending().and(Sort.by("name").ascending());
        List<TodoModel> todo = todoRepository.findAll(sort);
        return TodoDto.convert(todo);
    }

    @Transactional
    public void delete(UUID id){
        todoRepository.deleteById(id);
    }

    @Transactional
    public List<TodoDto> update(TodoDto todoDto){
        if(todoRepository.existsById(todoDto.getId())){
            todoRepository.save(convertDtoEntity(todoDto));
            return list();
        } else{
            throw new EntityNotFoundException("This task cannot be updated as it does not exist!");
        }
    }

    private TodoModel convertDtoEntity(TodoDto todoDto){
        TodoModel todoModel = new TodoModel();
        todoModel.setId(todoDto.getId());
        todoModel.setName(todoDto.getName());
        todoModel.setDescription(todoDto.getDescription());
        todoModel.setAccomplished(todoDto.isAccomplished());
        todoModel.setPriority(todoDto.getPriority());
        return todoModel;
    }
}