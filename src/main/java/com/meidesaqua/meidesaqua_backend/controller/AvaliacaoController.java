
package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.DTO.AvaliacaoDTO;
import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAvaliacao(@PathVariable Integer id, @RequestBody Avaliacao avaliacao, Authentication authentication) {
        try {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
            Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacao(id, avaliacao, usuarioLogado);
            return ResponseEntity.ok(avaliacaoAtualizada);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirAvaliacao(@PathVariable Integer id, Authentication authentication) {
        try {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
            avaliacaoService.excluirAvaliacao(id, usuarioLogado);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/estabelecimento/{id}")
    public ResponseEntity<List<AvaliacaoDTO>> listarPorEstabelecimento(@PathVariable Integer id) {
        List<AvaliacaoDTO> avaliacoesDTO = avaliacaoService.listarPorEstabelecimentoDTO(id);
        return ResponseEntity.ok(avaliacoesDTO);
    }
}