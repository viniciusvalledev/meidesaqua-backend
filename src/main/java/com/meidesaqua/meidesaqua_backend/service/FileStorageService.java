package com.meidesaqua.meidesaqua_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta para uploads!", e);
        }
    }

    // O metodo antigo pode ser mantido para outros usos ou removido se não for mais necessário
    public String save(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Files.copy(file.getInputStream(), this.root.resolve(uniqueFilename));
            return "/images/" + uniqueFilename;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível guardar a imagem. Erro: " + e.getMessage());
        }
    }

    // NOVO METODO PARA SALVAR A PARTIR DE BASE64
    public String saveBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        try {
            String[] parts = base64String.split(",");
            String imageString = parts.length > 1 ? parts[1] : parts[0];

            String extension = ".jpg"; // Extensão padrão
            if (parts.length > 1 && parts[0].contains("image/png")) {
                extension = ".png";
            } else if (parts.length > 1 && parts[0].contains("image/gif")) {
                extension = ".gif";
            }

            byte[] imageBytes = Base64.getDecoder().decode(imageString);
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path filePath = this.root.resolve(uniqueFilename);

            Files.write(filePath, imageBytes);

            return "/images/" + uniqueFilename;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível guardar a imagem a partir de Base64. Erro: " + e.getMessage());
        }
    }
}