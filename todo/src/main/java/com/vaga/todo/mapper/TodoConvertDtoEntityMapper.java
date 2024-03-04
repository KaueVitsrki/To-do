package com.vaga.todo.mapper;

import org.mapstruct.Mapper;

import com.vaga.todo.dto.TodoDto;
import com.vaga.todo.model.TodoModel;

@Mapper(componentModel = "spring")
public interface TodoConvertDtoEntityMapper {
    TodoModel convertDtoEntity(TodoDto todoDto);
}
