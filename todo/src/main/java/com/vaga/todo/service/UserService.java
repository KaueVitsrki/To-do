package com.vaga.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.UserDto;
import com.vaga.todo.exception.EmailAlreadyExistsException;
import com.vaga.todo.mapper.UserConvertDtoEntityMapper;
import com.vaga.todo.mapper.UserConvertEntityDtoMapper;
import com.vaga.todo.model.UserModel;
import com.vaga.todo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConvertEntityDtoMapper convertEntityDtoMapper;
    @Autowired
    private UserConvertDtoEntityMapper convertDtoEntityMapper;

    @Transactional
    public UserDto createUser(UserDto userDto){         
        if(userRepository.existsByEmail(userDto.getEmail())){
             throw new EmailAlreadyExistsException("Não foi possível realizar o cadastro, pois o Email já foi cadastrado");
        }

        UserModel userConvert = convertDtoEntityMapper.convertDtoEntity(userDto);
        String password = new BCryptPasswordEncoder().encode(userConvert.getPassword());
        userConvert.setPassword(password);
        userConvert.setTodoModel(new ArrayList<>());
        UserModel userSave = userRepository.save(userConvert);

        return convertEntityDtoMapper.convertEntityDto(userSave);
    }

    @Transactional
    public void deleteUser(UUID id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }else {
            throw new EntityNotFoundException("Não foi possível excluir a conta! O usuário não foi cadastrado.");
        } 
    }

    public List<UserDto> listUser(){
        List<UserModel> list = userRepository.findAll(); //Apagar
        return UserDto.convert(list);
    }

    public UserDto listUserId(UUID id){
        if(userRepository.existsById(id)){
            UserModel user = userRepository.findById(id).get();
            return convertEntityDtoMapper.convertEntityDto(user);
        }else {
            throw new EntityNotFoundException("Não foi possível listar o usuário! O usuário não está cadastrado.");
        }
    }

@Transactional
    public UserDto updateUser(UUID idUser, UserDto userDto){
        UserModel user = userRepository.findById(idUser)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + idUser));
    
        if(userRepository.existsByEmail(userDto.getEmail()) && !userDto.getEmail().equals(user.getEmail())){
            throw new EmailAlreadyExistsException("Não foi possivel realizar a troca de Email! Este Email já foi cadastrado");
        }

        String password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPassword(password);

        return convertEntityDtoMapper.convertEntityDto(user);
    } 
}
