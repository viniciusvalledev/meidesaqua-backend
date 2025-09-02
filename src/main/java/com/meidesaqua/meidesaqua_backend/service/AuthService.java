package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Quando o login é tentado, este metodo é chamado.
        System.out.println(">>> [AuthService] Procurando utilizador: " + username);
        return usuarioRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + username));
    }

    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.findByEmailOrUsername(usuario.getEmail(), usuario.getUsername()).isPresent()) {
            throw new Exception("Email ou nome de utilizador já cadastrado.");
        }

        // ====================== DEBUG ======================
        System.out.println(">>> [AuthService] Senha recebida para cadastro: " + usuario.getPassword());
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        System.out.println(">>> [AuthService] Senha APÓS criptografia: " + senhaCriptografada);
        // ===================================================

        usuario.setPassword(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }
}