package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Metodo customizado que o Spring Data JPA implementa automaticamente
    Optional<Usuario> findByEmailOrUsername(String email, String username);

    // Metodo para buscar o usuario pelo email
    Optional<Usuario> findByEmail(String email);

    // Metodo para alterar senha
    Optional<Usuario> findByResetPasswordToken(String token);

    // Metodo para enviar o token de confirmação
    Optional<Usuario> findByConfirmationToken(String confirmationToken);

    // Metodo para token de confirmação para troca de e-mail
    Optional<Usuario> findByEmailChangeToken(String token);

    Optional<Usuario> findByUsername(String username);
}