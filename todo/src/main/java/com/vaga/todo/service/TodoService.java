package com.vaga.todo.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.exception.NameTodoAlreadyExistsException;
import com.vaga.todo.exception.UnauthorizedAccessException;
import com.vaga.todo.mapper.TodoConvertDtoEntityMapper;
import com.vaga.todo.model.TodoModel;
import com.vaga.todo.model.UserModel;
import com.vaga.todo.repository.TodoRepository;
import com.vaga.todo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoConvertDtoEntityMapper todoConvertDtoEntityMapper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<TodoDto> createTodo(TodoDto todoDto){
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }
        UserModel userLogged = userLogged();
        String nameTodoDto =  todoDto.getName();
        boolean nameExist = userLogged.getTodoModel().stream().anyMatch(todo -> todo.getName().equals(nameTodoDto));

        if(nameExist){
            throw new NameTodoAlreadyExistsException("Não foi possível criar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }
        
        TodoModel todoModel = todoConvertDtoEntityMapper.convertDtoEntity(todoDto);
        todoModel.setUser(userLogged);
        userLogged.getTodoModel().add(todoModel);
        userRepository.save(userLogged);

        return listTodoUser();
    }

    public List<TodoDto> listTodoUser(){
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }
        UserModel userLogged = userLogged();
        List<TodoModel> list = userLogged.getTodoModel();

        list.sort(Comparator.comparing(TodoModel::getPriority).reversed().thenComparing(TodoModel::getName));
        return TodoDto.convert(list);
    }

    @Transactional
    public void deleteTodoUser(UUID idTodo) {
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }
        
        UserModel userLogged = userLogged();

        TodoModel todoToDelete = userLogged.getTodoModel().stream()
            .filter(todo -> todo.getId().equals(idTodo))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Não foi possível encontrar a tarefa com o ID: " + idTodo));

            userLogged.getTodoModel().remove(todoToDelete);
            userRepository.save(userLogged);
    }

    @Transactional
    public List<TodoDto> updateTodo(UUID idTodo, TodoDto todoDto){
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }
        if(todoRepository.existsByName(todoDto.getName())){
            throw new NameTodoAlreadyExistsException("Não foi possível atualizar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }

        TodoModel todoModel = todoRepository.findById(idTodo)
        .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o ID" + idTodo)); 

        UserModel userLogged = userLogged();
        String nameTodoDto =  todoDto.getName();
        boolean nameExist = userLogged.getTodoModel().stream().anyMatch(todo -> todo.getName().equals(nameTodoDto));

        if(nameExist){
            throw new NameTodoAlreadyExistsException("Não foi possível criar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }
        
        todoModel.setName(todoDto.getName());
        todoModel.setDescription(todoDto.getDescription());
        todoModel.setAccomplished(todoDto.isAccomplished());
        todoModel.setPriority(todoDto.getPriority());
        todoModel.setUser(userLogged);

        todoRepository.save(todoModel);
        userRepository.save(userLogged);

        return listTodoUser();
    }

    private UserModel userLogged(){        
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedUserEmail = userDetails.getUsername();
        UserModel userLogged = userRepository.findByEmail(loggedUserEmail);

        return userLogged;
    }

    private boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() != null;
    }

}