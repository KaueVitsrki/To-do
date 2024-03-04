package com.vaga.todo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vaga.todo.dto.UserDto;
import com.vaga.todo.mapper.ConvertDtoEntityMapper;
import com.vaga.todo.mapper.ConvertEntityDtoMapper;
import com.vaga.todo.model.UserModel;
import com.vaga.todo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;
    private ConvertEntityDtoMapper convertEntityDtoMapper;
    private ConvertDtoEntityMapper convertDtoEntityMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService(ConvertEntityDtoMapper convertEntityDtoMapper) {
        this.convertEntityDtoMapper = convertEntityDtoMapper;
    }

    @Transactional
    public UserDto saveUser(UserDto userDto){ 
        UserModel userSave = userRepository.save(convertDtoEntityMapper.convertDtoEntity(userDto));
        return convertEntityDtoMapper.convertEntityDto(userSave);
    }

    @Transactional
    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

    public List<UserDto> listUser(UserDto userDto){
        List<UserModel> list = userRepository.findAll();
        return UserDto.convert(list);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto){
        if(userRepository.existsById(userDto.getId())){
            UserModel userUpdate = userRepository.save(convertDtoEntityMapper.convertDtoEntity(userDto));
            return convertEntityDtoMapper.convertEntityDto(userUpdate);
        } else {
            return null;
        }
    }
}
