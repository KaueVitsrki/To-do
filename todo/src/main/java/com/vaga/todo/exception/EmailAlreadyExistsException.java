package com.vaga.todo.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    private String massage;

    public EmailAlreadyExistsException(String msg) {
        super(msg);
        this.massage = msg;
    }
}
