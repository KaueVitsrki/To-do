package com.vaga.todo.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.vaga.todo.model.TodoModel;
import com.vaga.todo.model.UserModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private List<TodoModel> todoModel;

    public UserDto(UserModel userModel) {
        this.email = userModel.getEmail();
        this.password = userModel.getPassword();
        this.todoModel = userModel.getTodoModel();
    }
    
    public static List<UserDto> convert(List<UserModel> userModel){
        return userModel.stream().map(UserDto::new).collect(Collectors.toList());
    }
}
