package com.vaga.todo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.UserDto;
import com.vaga.todo.exception.EmailAlreadyExistsException;
import com.vaga.todo.exception.UnauthorizedAccessException;
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

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
    public void deleteUser(UserDto userDto){
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }

        boolean passwordMatch = matches(userDto.getPassword(), userLogged().getPassword());
        boolean emailMatch = userDto.getEmail().equals(userLogged().getEmail());

        if(userRepository.existsById(userLogged().getId()) && passwordMatch && emailMatch){
            userRepository.deleteById(userLogged().getId());
        }else {
            throw new EntityNotFoundException("Não foi possível excluir a conta!");
        } 
    }

    public List<UserDto> listUser(){
        List<UserModel> list = userRepository.findAll(); //Apagar
        return UserDto.convert(list);
    }

    public UserDto listUserId(){
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }

        if(userRepository.existsById(userLogged().getId())){
            UserModel user = userRepository.findById(userLogged().getId()).get();
            return convertEntityDtoMapper.convertEntityDto(user);
        }else {
            throw new EntityNotFoundException("Não foi possível listar o usuário! O usuário não está cadastrado.");
        }
    }

    @Transactional
    public UserDto updateUser(UserDto userDto){
        if(!isUserLoggedIn()){
            throw new UnauthorizedAccessException("Usuário não autenticado");
        }

        if(userRepository.existsByEmail(userDto.getEmail()) && !userDto.getEmail().equals(userLogged().getEmail())){
            throw new EmailAlreadyExistsException("Não foi possivel realizar a troca de Email! Este Email já foi cadastrado");
        }

        UserModel userLogged = userLogged();
        String password = new BCryptPasswordEncoder().encode(userDto.getPassword());

        userLogged.setEmail(userDto.getEmail());
        userLogged.setPassword(password);

        return convertEntityDtoMapper.convertEntityDto(userLogged);
    }

    private boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() != null;
    } 

    private boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    private UserModel userLogged(){        
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedUserEmail = userDetails.getUsername();
        UserModel userLogged = userRepository.findByEmail(loggedUserEmail);

        return userLogged;
    }
}
