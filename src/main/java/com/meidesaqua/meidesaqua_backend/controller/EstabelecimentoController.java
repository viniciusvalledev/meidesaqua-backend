package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.EstabelecimentoDTO;
import com.meidesaqua.meidesaqua_backend.DTO.EstabelecimentoRequestDTO; // IMPORTAR
import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.service.AvaliacaoService;
import com.meidesaqua.meidesaqua_backend.service.EstabelecimentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estabelecimentos")
public class EstabelecimentoController {

    private static final Logger logger = LoggerFactory.getLogger(EstabelecimentoController.class);

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    // METODO DE CADASTRO VIA JSON
    @PostMapping
    public ResponseEntity<?> cadastrarEstabelecimentoJSON(@RequestBody EstabelecimentoRequestDTO requestDTO) {
        try {
            Estabelecimento novoEstabelecimento = estabelecimentoService.cadastrarEstabelecimentoComImagensBase64(requestDTO);
            return new ResponseEntity<>(novoEstabelecimento, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Falha ao cadastrar estabelecimento via JSON", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EstabelecimentoDTO>> buscarPorNome(@RequestParam String nome) {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.buscarPorNome(nome);
        List<EstabelecimentoDTO> dtos = estabelecimentos.stream()
                .map(estabelecimentoService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<EstabelecimentoDTO>> listarTodos() {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
        List<EstabelecimentoDTO> dtos = estabelecimentos.stream()
                .map(estabelecimentoService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoDTO> buscarPorId(@PathVariable Integer id) {
        return estabelecimentoService.buscarPorId(id)
                .map(estabelecimentoService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nomeFantasia}")
    public ResponseEntity<EstabelecimentoDTO> buscarPorNomeFantasia(@PathVariable String nomeFantasia) {
        return estabelecimentoService.buscarPorNomeFantasia(nomeFantasia)
                .map(estabelecimentoService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}