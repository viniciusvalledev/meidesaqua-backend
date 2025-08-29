// Em src/main/java/com/meidesaqua/meidesaquabackend/entity/Usuario.java
package com.meidesaqua.meidesaqua_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    // Garante que não haverá dois usuários com o mesmo username
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // Garante que não haverá dois usuários com o mesmo email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha; // Esta será a senha CRIPTOGRAFADA

}