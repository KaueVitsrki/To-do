package com.vaga.todo.exception;

public class TokenValidation extends RuntimeException{
    private String message;

    public TokenValidation(String msg){
        super(msg);
        this.message = msg;
    }
}
