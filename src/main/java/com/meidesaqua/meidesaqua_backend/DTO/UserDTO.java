package com.meidesaqua.meidesaqua_backend.DTO;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String nomeCompleto;
    // CAMPO REMOVIDO
    // private String chosenAvatar;

    public UserDTO(Usuario usuario) {
        this.id = usuario.getUsuarioId();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
        this.nomeCompleto = usuario.getNomeCompleto();
    }
}