package com.vaga.todo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.vaga.todo.config.TokenBlacklist;

@Service
public class InMemoryTokenBlacklist implements TokenBlacklist {
    private Set<String> blacklist = new HashSet<>();

    @Override
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
