package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.UserDTO;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest profileRequest, Authentication authentication) {
        try {
            Usuario updatedUser = authService.updateUserProfile(authentication.getName(), profileRequest);
            return ResponseEntity.ok(new UserDTO(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUserProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            authService.deleteUser(username);
            return ResponseEntity.ok(Collections.singletonMap("message", "Perfil de utilizador excluído com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdatePasswordRequest passwordRequest, Authentication authentication) {
        try {
            String username = authentication.getName();
            authService.updateUserPassword(username, passwordRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Senha alterada com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}