package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.service.AvaliacaoService;
import com.meidesaqua.meidesaqua_backend.service.EstabelecimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estabelecimentos")
public class EstabelecimentoController {

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    // Endpoint para CADASTRAR um novo estabelecimento
    @PostMapping
    public ResponseEntity<?> cadastrarEstabelecimento(@RequestBody Estabelecimento estabelecimento) {
        try {
            Estabelecimento novoEstabelecimento = estabelecimentoService.cadastrarEstabelecimento(estabelecimento);
            return new ResponseEntity<>(novoEstabelecimento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para BUSCAR estabelecimentos por nome (para a barra de pesquisa)
    @GetMapping("/buscar")
    public ResponseEntity<List<Estabelecimento>> buscarPorNome(@RequestParam String nome) {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarPorNome(nome);
        return ResponseEntity.ok(estabelecimentos);
    }

    // Endpoint para LISTAR TODOS os estabelecimentos
    @GetMapping
    public ResponseEntity<List<Estabelecimento>> listarTodos() {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
        return ResponseEntity.ok(estabelecimentos);
    }

    // Endpoint para BUSCAR UM estabelecimento pelo seu ID
    @GetMapping("/{id}")
    public ResponseEntity<Estabelecimento> buscarPorId(@PathVariable Integer id) {
        return estabelecimentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para CALCULAR A MÉDIA de avaliações de um estabelecimento
    @GetMapping("/{id}/media")
    public ResponseEntity<Map<String, Object>> getMediaAvaliacoes(@PathVariable Integer id) {
        Double media = avaliacaoService.calcularMediaPorEstabelecimento(id);

        if (media == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("media", 0.0);
            response.put("mensagem", "Este estabelecimento ainda não possui avaliações.");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(Map.of("media", media));
    }

    // Endpoint para ATIVAR ou DESATIVAR um estabelecimento
    @PostMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> statusMap) {
        try {
            Boolean novoStatus = statusMap.get("ativo");
            if (novoStatus == null) {
                return new ResponseEntity<>("O corpo da requisição deve conter a chave 'ativo' com um valor booleano (true/false).", HttpStatus.BAD_REQUEST);
            }

            Estabelecimento estabelecimentoAtualizado = estabelecimentoService.alterarStatusAtivo(id, novoStatus);
            return ResponseEntity.ok(estabelecimentoAtualizado);

        } catch (Exception e) {
            // Retorna Not Found se o estabelecimento com o ID não existir
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}