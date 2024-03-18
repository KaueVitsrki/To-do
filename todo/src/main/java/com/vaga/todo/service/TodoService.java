package com.vaga.todo.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.exception.NameTodoAlreadyExistsException;
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
    public List<TodoDto> createTodo(UUID idUser, TodoDto todoDto){
        if(todoRepository.existsByName(todoDto.getName())){
            throw new NameTodoAlreadyExistsException("Não foi possível criar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }

        UserModel user = userRepository.findById(idUser)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + idUser));
        TodoModel todoModel = todoConvertDtoEntityMapper.convertDtoEntity(todoDto);
        
        todoModel.setUser(user);
        user.getTodoModel().add(todoModel);
        userRepository.save(user);

        return listTodoUser(idUser);
    }

    public List<TodoDto> listTodoUser(UUID idUser){
        UserModel user = userRepository.findById(idUser)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID:" + idUser));

        List<TodoModel> list = user.getTodoModel();

        list.sort(Comparator.comparing(TodoModel::getPriority).reversed().thenComparing(TodoModel::getName));
        return TodoDto.convert(list);
    }

    @Transactional
    public void deleteTodoUser(UUID idUser, UUID idTodo) {
        UserModel user = userRepository.findById(idUser)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + idUser));

        TodoModel todoToDelete = user.getTodoModel().stream()
            .filter(todo -> todo.getId().equals(idTodo))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Não foi possível encontrar a tarefa com o ID: " + idTodo));

            user.getTodoModel().remove(todoToDelete);
            userRepository.save(user);
    }

    @Transactional
    public List<TodoDto> updateTodo(UUID idUser, UUID idTodo, TodoDto todoDto){
        if(todoRepository.existsByName(todoDto.getName())){
            throw new NameTodoAlreadyExistsException("Não foi possível atualizar a tarefa! Já existe uma tarefa com o mesmo nome.");
        }

        TodoModel todoModel = todoRepository.findById(idTodo)
        .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o ID" + idTodo)); 
        
        if(todoModel.getUser().getId().equals(idUser)){
            throw new EntityNotFoundException("Essa tarefa não pode ser atualizada, pois não pertence ao usuário!");
        }
        
        UserModel user = userRepository.findById(idUser)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + idUser));

        todoModel.setName(todoDto.getName());
        todoModel.setDescription(todoDto.getDescription());
        todoModel.setAccomplished(todoDto.isAccomplished());
        todoModel.setPriority(todoDto.getPriority());
        todoModel.setUser(user);

        todoRepository.save(todoModel);
        userRepository.save(user);

        return listTodoUser(idUser);
    }
}