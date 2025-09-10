package com.meidesaqua.meidesaqua_backend.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfanityFilterService {

    // Adicione aqui as palavras que você deseja bloquear
    private static final List<String> PALAVRAS_PROIBIDAS = Arrays.asList(
            "palavrao1", "palavrao2", "palavrao3", "etc"
            // Adicione quantas palavras quiser, sempre em minúsculas
    );


    public boolean contemPalavrao(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        String textoEmMinusculo = texto.toLowerCase();
        return PALAVRAS_PROIBIDAS.stream().anyMatch(textoEmMinusculo::contains);
    }
}