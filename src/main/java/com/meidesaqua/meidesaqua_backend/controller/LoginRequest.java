package com.meidesaqua.meidesaqua_backend.controller;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}