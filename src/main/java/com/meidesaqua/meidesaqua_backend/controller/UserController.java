package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.UserDTO;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthService authService;

    // O PasswordEncoder foi removido pois não é mais usado neste controller
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // Endpoint: PUT /api/users/profile
    @PostMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest profileRequest, Authentication authentication) {
        try {
            // O 'authentication.getName()' pega o username do utilizador logado a partir do token JWT
            Usuario updatedUser = authService.updateUserProfile(authentication.getName(), profileRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // NOVO ENDPOINT PARA EXCLUSÃO DE PERFIL
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUserProfile(Authentication authentication) {
        try {
            // Pega o nome de utilizador a partir do token de autenticação
            String username = authentication.getName();
            authService.deleteUser(username);
            return ResponseEntity.ok("Perfil de utilizador excluído com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}