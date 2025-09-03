package com.meidesaqua.meidesaqua_backend.controller;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String email;
    // Adicione aqui outros campos que queira permitir a atualização, como nomeCompleto
    private String nomeCompleto;
}