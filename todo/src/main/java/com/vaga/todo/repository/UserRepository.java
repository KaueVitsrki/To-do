package com.vaga.todo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaga.todo.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{
    boolean existsByEmail(String email);
    UserModel findByEmail(String email);
}
