package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // URL base para autenticação
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint para CADASTRAR um novo utilizador
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = authService.cadastrarUsuario(usuario);
            return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para LOGIN de um utilizador existente
    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(Authentication authentication) {
        // Se a requisição chegar até aqui, o Spring Security já validou o username e a senha.
        // O objeto 'authentication' contém os detalhes do utilizador que foi autenticado com sucesso.
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // Retorna os dados do utilizador logado como confirmação de sucesso.
        return ResponseEntity.ok(usuarioLogado);
    }
}

