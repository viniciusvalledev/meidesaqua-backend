// Local: src/main/java/com/meidesaqua/meidesaquabackend/repository/UsuarioRepository.java
package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Método customizado que o Spring Data JPA implementa automaticamente
    Optional<Usuario> findByEmailOrUsername(String email, String username);
}