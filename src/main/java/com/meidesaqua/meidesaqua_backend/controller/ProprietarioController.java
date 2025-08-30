package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.entity.Proprietario;
import com.meidesaqua.meidesaqua_backend.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proprietarios") // URL base para tudo relacionado a proprietários
public class ProprietarioController {

    @Autowired
    private ProprietarioService proprietarioService;

    // Endpoint para cadastrar um novo proprietário
    @PostMapping
    public ResponseEntity<?> cadastrarProprietario(@RequestBody Proprietario proprietario) {
        try {
            Proprietario proprietarioSalvo = proprietarioService.cadastrarProprietario(proprietario);
            return new ResponseEntity<>(proprietarioSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Futuramente, podemos adicionar aqui os endpoints de busca para proprietários
    // Ex: @GetMapping, @GetMapping("/{id}"), etc.
}