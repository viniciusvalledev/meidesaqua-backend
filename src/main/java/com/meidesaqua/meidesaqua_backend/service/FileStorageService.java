package com.meidesaqua.meidesaqua_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            // Cria a pasta "uploads" se ela não existir
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta para uploads!", e);
        }
    }

    public String save(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            // Pega a extensão do ficheiro (ex: ".jpg")
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // Cria um nome de ficheiro único para evitar conflitos
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Copia o ficheiro para a pasta "uploads"
            Files.copy(file.getInputStream(), this.root.resolve(uniqueFilename));

            // Retorna o caminho que será usado para aceder à imagem pela web
            return "/images/" + uniqueFilename;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível guardar a imagem. Erro: " + e.getMessage());
        }
    }
}