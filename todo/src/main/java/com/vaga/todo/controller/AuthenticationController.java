package com.vaga.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaga.todo.dto.AuthenticationDto;
import com.vaga.todo.dto.LoginResponseDto;
import com.vaga.todo.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto authenticationDto){
        LoginResponseDto loginResponseDto = tokenService.login(authenticationDto);
        return ResponseEntity.ok(loginResponseDto);
    }
}
