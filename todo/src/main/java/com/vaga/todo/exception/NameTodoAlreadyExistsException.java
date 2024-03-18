package com.vaga.todo.exception;

public class NameTodoAlreadyExistsException  extends RuntimeException{
    private String message;

    public NameTodoAlreadyExistsException(String msg){
        super(msg);
        this.message = msg;
    }
}
