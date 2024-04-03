package com.vaga.todo.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.exception.NameTodoAlreadyExistsException;
import com.vaga.todo.mapper.TodoConvertDtoEntityMapper;
import com.vaga.todo.mapper.TodoConvertEntityDtoMapper;
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
    private TodoConvertEntityDtoMapper todoConvertEntityDtoMapper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TodoDto createTodo(TodoDto todoDto, JwtAuthenticationToken token){
        UserModel userLogged = userLogged(token);
        boolean nameExist = userLogged.getTodoModel().stream().anyMatch(todo -> todo.getName().equals(todoDto.getName()));

        if(nameExist){
            throw new NameTodoAlreadyExistsException("Não foi possível criar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }
        if(todoDto.getId() != null){
            throw new RuntimeException("Não foi possível criar a tarefa, não é aceito id como parâmetro");
        }
        
        TodoModel todoModel = todoConvertDtoEntityMapper.convertDtoEntity(todoDto);
        todoModel.setUser(userLogged);
        userLogged.getTodoModel().add(todoModel);
        userRepository.save(userLogged);

        return todoConvertEntityDtoMapper.convertEntityDto(todoModel);
    }

    public List<TodoDto> listTodoUser(JwtAuthenticationToken token){
        UserModel userLogged = userLogged(token);
        List<TodoModel> list = userLogged.getTodoModel();
        list.sort(Comparator.comparing(TodoModel::getPriority).reversed().thenComparing(TodoModel::getName));
        
        return TodoDto.convert(list);
    }

    @Transactional
    public void deleteTodoUser(UUID idTodo, JwtAuthenticationToken token) {
        UserModel userLogged = userLogged(token);

        TodoModel todoToDelete = userLogged.getTodoModel().stream()
            .filter(todo -> todo.getId().equals(idTodo))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Não foi possível encontrar a tarefa com o ID: " + idTodo));

            userLogged.getTodoModel().remove(todoToDelete);
            userRepository.save(userLogged);
    }

    @Transactional
    public TodoDto updateTodo(UUID idTodo, TodoDto todoDto, JwtAuthenticationToken token){
        TodoModel todoModel = todoRepository.findById(idTodo)
        .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o ID" + idTodo)); 

        UserModel userLogged = userLogged(token);
        String nameTodoDto =  todoDto.getName();
        boolean nameExist = userLogged.getTodoModel()
        .stream()
        .anyMatch(todo -> todo.getName().equals(nameTodoDto) && !todo.getId().equals(todoDto.getId()));
            
        if(nameExist){
            throw new NameTodoAlreadyExistsException("Não foi possível atualizar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }
        if(todoDto.getId() != null){
            throw new RuntimeException("Não foi possível criar a tarefa, não é aceito id como parâmetro");
        }
        
        todoModel.setName(todoDto.getName());
        todoModel.setDescription(todoDto.getDescription());
        todoModel.setAccomplished(todoDto.isAccomplished());
        todoModel.setPriority(todoDto.getPriority());
        todoModel.setUser(userLogged);
        todoRepository.save(todoModel);
        userRepository.save(userLogged);

        return todoConvertEntityDtoMapper.convertEntityDto(todoModel);
    }

    private UserModel userLogged(JwtAuthenticationToken token){        
        return userRepository.findById(UUID.fromString(token.getName()))
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }
}