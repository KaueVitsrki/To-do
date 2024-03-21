package com.vaga.todo.exception;

public class UnauthorizedAccessException extends RuntimeException{
    private String message;

    public UnauthorizedAccessException(String msg){
        super(msg);
        this.message = msg;
    }
}
