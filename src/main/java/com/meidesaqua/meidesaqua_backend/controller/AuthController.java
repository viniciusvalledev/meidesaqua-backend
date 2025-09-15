package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.LoginResponseDTO;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AuthService;
import com.meidesaqua.meidesaqua_backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
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

    // ----- A ÚNICA ALTERAÇÃO NECESSÁRIA É AQUI -----
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) throws Exception {
        authService.cadastrarUsuario(usuario, passwordEncoder);

        // --- ALTERAÇÃO AQUI ---
        // Agora, a resposta de sucesso também é um JSON.
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cadastro realizado com sucesso! Por favor, verifique seu e-mail para ativar sua conta.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            final Usuario usuario = (Usuario) authService.loadUserByUsername(loginRequest.getUsername());
            final String token = jwtService.generateToken(usuario);

            return ResponseEntity.ok(new LoginResponseDTO(token, usuario));

        } catch (DisabledException e) {
            return new ResponseEntity<>("Conta não ativada. Por favor, verifique seu e-mail de confirmação.", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Utilizador ou senha inválidos.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String token) {
        try {
            authService.confirmUserAccount(token);
            // CORREÇÃO: Retorna uma resposta JSON para sucesso
            return ResponseEntity.ok(Collections.singletonMap("message", "Conta ativada com sucesso. Você já pode fazer login."));
        } catch (Exception e) {
            // BOA PRÁTICA: Retorna uma resposta JSON para erro
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            authService.forgotPassword(email);
            return ResponseEntity.ok(Collections.singletonMap("message", "Se existir uma conta com o e-mail fornecido, um link de redefinição de senha foi enviado."));
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Se existir uma conta com o e-mail fornecido, um link de redefinição de senha foi enviado."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Collections.singletonMap("message", "Senha redefinida com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NOVO ENDPOINT
    @GetMapping("/confirm-email-change")
    public ResponseEntity<?> confirmEmailChange(@RequestParam("token") String token) {
        try {
            authService.confirmEmailChange(token);
            return ResponseEntity.ok("Alteração de e-mail confirmada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}