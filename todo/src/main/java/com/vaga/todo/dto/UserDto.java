package com.vaga.todo.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vaga.todo.model.TodoModel;
import com.vaga.todo.model.UserModel;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserDto {
    private UUID id;
    private Email email;
    private List<TodoModel> todoModel;

    public UserDto(UserModel userModel) {
        this.id = userModel.getId();
        this.email = userModel.getEmail();
        this.todoModel = userModel.getTodoModel();
    }
    
    public static List<UserDto> convert(List<UserModel> userModel){
        return userModel.stream().map(UserDto::new).collect(Collectors.toList());
    }
}
