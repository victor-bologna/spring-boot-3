package com.example.springboot.domain.services.impl;

import com.example.springboot.domain.models.User;
import com.example.springboot.domain.services.UserService;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
