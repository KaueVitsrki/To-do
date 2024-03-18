package com.vaga.todo.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.vaga.todo.exception.TokenValidation;
import com.vaga.todo.model.UserModel;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModel userModel){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT
            .create()
            .withIssuer("apitodo")
            .withSubject(userModel.getUsername())
            .withExpiresAt(getExpirationDate())
            .sign(algorithm);

            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Não foi possivel criar o token", exception);
        }
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT
            .require(algorithm)
            .withIssuer("apitodo")
            .build()
            .verify(token)
            .getSubject();
        } catch (JWTCreationException exception){
            throw new TokenValidation("Token inválido");
        }
    }
}
