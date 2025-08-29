package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.service.EstabelecimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estabelecimentos")
public class EstabelecimentoController {

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    // --- NOVO ENDPOINT DE CADASTRO ---
    @PostMapping
    public ResponseEntity<?> cadastrarEstabelecimento(@RequestBody Estabelecimento estabelecimento) {
        try {
            Estabelecimento novoEstabelecimento = estabelecimentoService.cadastrarEstabelecimento(estabelecimento);
            return new ResponseEntity<>(novoEstabelecimento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // Endpoint para buscar estabelecimentos por nome.
    // Exemplo de como chamar: /api/estabelecimentos/buscar?nome=pizzaria
    @GetMapping("/buscar")
    public ResponseEntity<List<Estabelecimento>> buscarPorNome(@RequestParam String nome) {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarPorNome(nome);
        return ResponseEntity.ok(estabelecimentos);
    }

    // Endpoint para listar TODOS os estabelecimentos.
    // Exemplo de como chamar: /api/estabelecimentos
    @GetMapping
    public ResponseEntity<List<Estabelecimento>> listarTodos() {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
        return ResponseEntity.ok(estabelecimentos);
    }

    // Endpoint para buscar UM estabelecimento pelo seu ID.
    // Exemplo de como chamar: /api/estabelecimentos/1
    @GetMapping("/{id}")
    public ResponseEntity<Estabelecimento> buscarPorId(@PathVariable Integer id) {
        return estabelecimentoService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o estabelecimento no corpo
                .orElse(ResponseEntity.notFound().build()); // Se não encontrar, retorna 404 Not Found
    }
}