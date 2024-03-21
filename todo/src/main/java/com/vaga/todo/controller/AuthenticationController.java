package com.vaga.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaga.todo.dto.AuthenticationDto;
import com.vaga.todo.dto.TokenDto;
import com.vaga.todo.model.UserModel;
import com.vaga.todo.service.InMemoryTokenBlacklist;
import com.vaga.todo.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private InMemoryTokenBlacklist inMemoryTokenBlacklist;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto dados){
        var usarnamePassword = new UsernamePasswordAuthenticationToken(
            dados.getEmail(),
            dados.getPassword());
        var auth = manager.authenticate(usarnamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());
        return ResponseEntity.ok(new TokenDto(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = tokenService.extractTokenFromRequest(request);
        inMemoryTokenBlacklist.addToBlacklist(token);

        return ResponseEntity.ok("Logged out successfully");
    }
}
