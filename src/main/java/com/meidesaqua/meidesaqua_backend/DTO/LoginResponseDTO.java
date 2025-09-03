package com.meidesaqua.meidesaqua_backend.DTO;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private UserDTO user;

    public LoginResponseDTO(String token, Usuario usuario) {
        this.token = token;
        this.user = new UserDTO(usuario);
    }
}