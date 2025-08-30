package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// IMPORTANTE: Adicione "implements UserDetailsService"
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Metodo para verificarf o login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O Spring Security chama este metodo para encontrar um usuário pelo username
        return usuarioRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.findByEmailOrUsername(usuario.getEmail(), usuario.getUsername()).isPresent()) {
            throw new Exception("Email ou nome de usuário já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }
}