package com.meidesaqua.meidesaqua_backend.controller;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String currentPassword;
    private String newPassword;
} 