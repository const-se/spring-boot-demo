package com.example.demo.enums;

public enum Role {
    ROLE_USER("Пользователь"),
    ROLE_ADMIN("Администратор");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
