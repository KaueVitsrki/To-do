package com.vaga.todo.mapper;

import org.mapstruct.Mapper;

import com.vaga.todo.dto.UserDto;
import com.vaga.todo.model.UserModel;

@Mapper(componentModel = "spring")
public interface ConvertDtoEntityMapper {
    UserModel convertDtoEntity(UserDto userDto);
}
