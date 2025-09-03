package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AuthService;
import com.meidesaqua.meidesaqua_backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = authService.cadastrarUsuario(usuario, passwordEncoder);
            return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            final UserDetails userDetails = authService.loadUserByUsername(loginRequest.getUsername());
            final String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return new ResponseEntity<>("Utilizador ou senha inválidos.", HttpStatus.UNAUTHORIZED);
        }
    }

    // NOVO ENDPOINT: SOLICITAR REDEFINIÇÃO DE SENHA
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        authService.createPasswordResetTokenForUser(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "Se o e-mail estiver cadastrado, um link de redefinição será enviado."));
    }

    // NOVO ENDPOINT: REDEFINIR A SENHA
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            String newPassword = body.get("newPassword");
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}