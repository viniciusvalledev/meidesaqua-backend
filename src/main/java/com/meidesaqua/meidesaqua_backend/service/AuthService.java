// Local: src/main/java/com/meidesaqua/meidesaquabackend/service/AuthService.java
package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        // Regra de Negócio: Não permitir cadastros com email ou username duplicados.
        if (usuarioRepository.findByEmailOrUsername(usuario.getEmail(), usuario.getUsername()).isPresent()) {
            throw new Exception("Email ou nome de usuário já cadastrado.");
        }

        // TODO: Futuramente, aqui entrará a criptografia da senha antes de salvar.

        // Se a regra de negócio passar, chama o repositório para salvar.
        return usuarioRepository.save(usuario);
    }
}
