package com.vaga.todo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ResponseEntity<List<UserDto>> listUser(){ // Alterar para somente o usuario ver seus parâmetros
        List<UserDto> list = userService.listUser();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> listUserById(@Valid @PathVariable UUID id){
        UserDto listUserById = userService.listUserId(id);
        return ResponseEntity.ok(listUserById);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto){
        UserDto userCreate = userService.createUser(userDto);
        return new ResponseEntity<UserDto>(userCreate, HttpStatus.CREATED);
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity deleteUser(@PathVariable UUID idUser){
        userService.deleteUser(idUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID idUser, @RequestBody @Valid UserDto userDto){
        UserDto userUpdate = userService.updateUser(idUser, userDto);
        return ResponseEntity.ok(userUpdate);       
    }
}
