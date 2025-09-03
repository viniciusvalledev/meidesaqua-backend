package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.UserDTO; // IMPORTAR DTO
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Novo URL base para gestão de utilizadores
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint: PUT /api/users/profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest profileRequest, Authentication authentication) {
        try {
            // O 'authentication.getName()' pega o username do utilizador logado a partir do token JWT
            Usuario updatedUser = authService.updateUserProfile(authentication.getName(), profileRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint: PUT /api/users/password
    @PutMapping("/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdatePasswordRequest passwordRequest, Authentication authentication) {
        try {
            authService.updateUserPassword(authentication.getName(), passwordRequest, passwordEncoder);
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NOVO ENDPOINT PARA ATUALIZAR O AVATAR
    @PutMapping("/avatar")
    public ResponseEntity<?> updateUserAvatar(@RequestBody UpdateAvatarRequest avatarRequest, Authentication authentication) {
        try {
            // Pega o username do usuário logado a partir do token
            String username = authentication.getName();

            // Chama o serviço para atualizar o avatar no banco de dados
            Usuario updatedUser = authService.updateUserAvatar(username, avatarRequest.getAvatar());

            // Retorna o usuário atualizado (convertido para DTO para segurança)
            return ResponseEntity.ok(new UserDTO(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}