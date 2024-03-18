package com.vaga.todo.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="todos")
@Data
@AllArgsConstructor @NoArgsConstructor
public class TodoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank 
    @Size(min = 3)
    private String name;

    @NotBlank
    @Size(min = 3)
    private String description;

    private boolean accomplished;
    
    @NotNull
    @Min(0) @Max(5)
    private int priority;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserModel user;
}
