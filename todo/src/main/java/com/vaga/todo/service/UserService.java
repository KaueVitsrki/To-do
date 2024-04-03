package com.vaga.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.vaga.todo.dto.UserDto;
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
        UserModel userConvert = convertDtoEntityMapper.convertDtoEntity(userDto);
        String password = new BCryptPasswordEncoder().encode(userConvert.getPassword());
        userConvert.setPassword(password);
        userConvert.setTodoModel(new ArrayList<>());
        UserModel userSave = userRepository.save(userConvert);

        return convertEntityDtoMapper.convertEntityDto(userSave);
    }

    @Transactional
    public void deleteUser(UserDto userDto, JwtAuthenticationToken token){
        boolean passwordMatch = matches(userDto.getPassword(), userLogged(token).getPassword());
        boolean emailMatch = userDto.getEmail().equals(userLogged(token).getEmail());

        if(userRepository.existsById(userLogged(token).getId()) && passwordMatch && emailMatch){
            userRepository.deleteById(userLogged(token).getId());
        }else {
            throw new EntityNotFoundException("Não foi possível excluir a conta!");
        } 
    }

    public List<UserDto> listUser(){
        List<UserModel> list = userRepository.findAll(); //Apagar
        return UserDto.convert(list);
    }

    public UserDto listUserId(JwtAuthenticationToken token){
        if(userRepository.existsById(userLogged(token).getId())){
            UserModel user = userRepository.findById(userLogged(token).getId()).get();
            return convertEntityDtoMapper.convertEntityDto(user);
        }else {
            throw new EntityNotFoundException("Não foi possível listar o usuário! O usuário não está cadastrado.");
        }
    }

    @Transactional
    public UserDto updateUser(UserDto userDto, JwtAuthenticationToken token){
        UserModel userLogged = userLogged(token);
        String password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        userLogged.setEmail(userDto.getEmail());
        userLogged.setPassword(password);

        return convertEntityDtoMapper.convertEntityDto(userLogged);
    }

    private UserModel userLogged(JwtAuthenticationToken token){        
        return userRepository.findById(UUID.fromString(token.getName()))
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
