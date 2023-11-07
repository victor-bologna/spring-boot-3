package com.example.springboot.domain.services;

import com.example.springboot.domain.models.User;

public interface TokenService {
    String generateToken(User user);

    String validateToken(String token);

}
