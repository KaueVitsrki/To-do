package com.vaga.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaga.todo.dto.UserDto;
import com.vaga.todo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> listUser(){
        List<UserDto> list = userService.listUser();// apagar
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<UserDto> listUserById(JwtAuthenticationToken token){
        UserDto listUserById = userService.listUserId(token);
        return ResponseEntity.ok(listUserById);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        UserDto userCreate = userService.createUser(userDto, token);
        return new ResponseEntity<UserDto>(userCreate, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        userService.deleteUser(userDto, token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        UserDto userUpdate = userService.updateUser(userDto, token);
        return ResponseEntity.ok(userUpdate);       
    }
}
