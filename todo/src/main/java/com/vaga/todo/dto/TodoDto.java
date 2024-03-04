package com.vaga.todo.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vaga.todo.model.TodoModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class TodoDto {
    private UUID id;
    private String name;
    private String description;
    private boolean accomplished;
    private int priority;

    public TodoDto(TodoModel todoModel) {
        this.id = todoModel.getId();
        this.name = todoModel.getName();
        this.description = todoModel.getDescription();
        this.accomplished = todoModel.isAccomplished();
        this.priority = todoModel.getPriority();
    }

    public static List<TodoDto> convert(List<TodoModel> todoModels){
        return todoModels.stream().map(TodoDto::new).collect(Collectors.toList());
    }
}
