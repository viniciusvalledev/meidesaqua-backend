package com.meidesaqua.meidesaqua_backend.controller; // Verifique se o nome do seu pacote está correto

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
