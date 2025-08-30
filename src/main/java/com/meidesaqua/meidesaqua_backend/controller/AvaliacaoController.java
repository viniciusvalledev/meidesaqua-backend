package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes") // URL base para tudo relacionado a avaliações
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // Endpoint para um utilizador submeter uma nova avaliação
    @PostMapping
    public ResponseEntity<Avaliacao> submeterAvaliacao(@RequestBody Avaliacao avaliacao) {
        try {
            Avaliacao novaAvaliacao = avaliacaoService.submeterAvaliacao(avaliacao);
            return new ResponseEntity<>(novaAvaliacao, HttpStatus.CREATED);
        } catch (Exception e) {
            // Em caso de erro (ex: utilizador ou estabelecimento não existem), retorna um erro genérico
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}