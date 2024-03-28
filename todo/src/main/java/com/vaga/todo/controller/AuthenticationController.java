package com.vaga.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaga.todo.dto.AuthenticationDto;
import com.vaga.todo.dto.LoginResponseDto;
import com.vaga.todo.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        try{
            String auth = request.getHeader("Authorization");

            if (auth != null && auth.contains("Bearer")) {
                auth.replace("Bearer", "");
            }
        } catch (Exception e) {
        return ResponseEntity.badRequest().body("Invalid access token");
        }

    return ResponseEntity.ok().body("Access token invalidated successfully");
    }
}
