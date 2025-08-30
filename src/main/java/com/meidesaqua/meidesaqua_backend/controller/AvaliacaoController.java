package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // Endpoint para um utilizador LOGADO submeter uma nova avaliação
    @PostMapping
    public ResponseEntity<?> submeterAvaliacao(@RequestBody Avaliacao avaliacao, Authentication authentication) {
        try {
            // Pega o objeto do utilizador que está autenticado
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

            // Chama o serviço, passando a avaliação e o utilizador logado
            Avaliacao novaAvaliacao = avaliacaoService.submeterAvaliacao(avaliacao, usuarioLogado);

            return new ResponseEntity<>(novaAvaliacao, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}