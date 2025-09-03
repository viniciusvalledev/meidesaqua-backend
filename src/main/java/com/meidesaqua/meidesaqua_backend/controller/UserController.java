package com.meidesaqua.meidesaqua_backend.controller;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest profileRequest, Authentication authentication) {
        try {
            Usuario updatedUser = authService.updateUserProfile(authentication.getName(), profileRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdatePasswordRequest passwordRequest, Authentication authentication) {
        try {
            authService.updateUserPassword(authentication.getName(), passwordRequest, passwordEncoder);
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NOVO ENDPOINT: DELETAR PERFIL
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUserProfile(Authentication authentication) {
        try {
            authService.deleteUser(authentication.getName());
            return ResponseEntity.ok("Utilizador deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}