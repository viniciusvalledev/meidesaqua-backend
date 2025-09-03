package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Metodo customizado que o Spring Data JPA implementa automaticamente
    Optional<Usuario> findByEmailOrUsername(String email, String username);

    // Metodo para buscar o usuario pelo email
    Optional<Usuario> findByEmail(String email);
}