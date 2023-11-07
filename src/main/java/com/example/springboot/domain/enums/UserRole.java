package com.example.springboot.domain.enums;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private final String role;
    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
