package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.AvaliacaoDTO; // IMPORTAR O DTO
import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // (o metodo submeterAvaliacao não precisa de alteração)
    @PostMapping
    public ResponseEntity<?> submeterAvaliacao(@RequestBody Avaliacao avaliacao, Authentication authentication) {
        try {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
            Avaliacao novaAvaliacao = avaliacaoService.submeterAvaliacao(avaliacao, usuarioLogado);
            return new ResponseEntity<>(novaAvaliacao, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Endpoint para LISTAR TODAS as avaliações de um estabelecimento específico
    // ALTERAÇÃO: Agora retorna uma lista de AvaliacaoDTO
    @GetMapping("/estabelecimento/{id}")
    public ResponseEntity<List<AvaliacaoDTO>> listarPorEstabelecimento(@PathVariable Integer id) {
        List<AvaliacaoDTO> avaliacoesDTO = avaliacaoService.listarPorEstabelecimentoDTO(id);
        return ResponseEntity.ok(avaliacoesDTO);
    }
}