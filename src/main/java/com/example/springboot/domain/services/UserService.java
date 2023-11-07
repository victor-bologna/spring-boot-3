package com.example.springboot.domain.services;

import com.example.springboot.domain.models.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    User save(User newUser);

    UserDetails findByLogin(String login);
}
