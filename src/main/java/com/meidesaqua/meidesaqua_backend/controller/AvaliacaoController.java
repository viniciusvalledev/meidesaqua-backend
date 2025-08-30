package com.meidesaqua.meidesaqua_backend.controller;

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

    // Endpoint para um UTILIZADOR LOGADO submeter uma nova avaliação
    @PostMapping
    public ResponseEntity<?> submeterAvaliacao(@RequestBody Avaliacao avaliacao, Authentication authentication) {
        try {
            // Pega o objeto do utilizador que está autenticado na sessão
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

            // Chama o serviço, passando a avaliação e o utilizador logado
            Avaliacao novaAvaliacao = avaliacaoService.submeterAvaliacao(avaliacao, usuarioLogado);

            return new ResponseEntity<>(novaAvaliacao, HttpStatus.CREATED);
        } catch (Exception e) {
            // Retorna a mensagem de erro específica do serviço (ex: "Utilizador já avaliou")
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para LISTAR TODAS as avaliações de um estabelecimento específico
    @GetMapping("/estabelecimento/{id}")
    public ResponseEntity<List<Avaliacao>> listarPorEstabelecimento(@PathVariable Integer id) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarPorEstabelecimento(id);
        return ResponseEntity.ok(avaliacoes);
    }
}