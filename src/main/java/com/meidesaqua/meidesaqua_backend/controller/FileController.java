package com.meidesaqua.meidesaqua_backend.controller;

import com.meidesaqua.meidesaqua_backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    // Endpoint para um único ficheiro (para a logo)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileStorageService.save(file);
            return ResponseEntity.ok(Collections.singletonMap("url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Falha ao fazer o upload da imagem: " + e.getMessage());
        }
    }

    // NOVO ENDPOINT PARA MÚLTIPLOS FICHEIROS (para o carrossel)
    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            List<String> urls = Arrays.stream(files)
                    .map(file -> fileStorageService.save(file))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Collections.singletonMap("urls", urls));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Falha ao fazer o upload das imagens: " + e.getMessage());
        }
    }
}