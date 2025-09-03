package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // CORREÇÃO: Altere de "FindByToken" para "findByToken"
    Optional<PasswordResetToken> findByToken(String token);
}