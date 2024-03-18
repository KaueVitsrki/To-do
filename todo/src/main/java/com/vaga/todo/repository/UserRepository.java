package com.vaga.todo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaga.todo.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{
    boolean existsByEmail(String email);
    
    UserDetails findByEmail(String email);   
}
