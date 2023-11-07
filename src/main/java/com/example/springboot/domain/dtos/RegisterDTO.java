package com.example.springboot.domain.dtos;

import com.example.springboot.domain.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
