package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.controller.UpdatePasswordRequest;
import com.meidesaqua.meidesaqua_backend.controller.UpdateProfileRequest;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.AvaliacaoRepository;
import com.meidesaqua.meidesaqua_backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
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
    private AvaliacaoRepository avaliacaoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + username));
    }

    public Usuario cadastrarUsuario(Usuario usuario, PasswordEncoder passwordEncoder) throws Exception {
        if (usuarioRepository.findByEmailOrUsername(usuario.getEmail(), usuario.getUsername()).isPresent()) {
            throw new Exception("Email ou nome de utilizador já cadastrado.");
        }
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUserProfile(String currentUsername, UpdateProfileRequest data) throws Exception {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new Exception("Utilizador não encontrado."));
        if (data.getNomeCompleto() != null) {
            currentUser.setNomeCompleto(data.getNomeCompleto());
        }
        if (data.getEmail() != null) {
            currentUser.setEmail(data.getEmail());
        }
        if (data.getUsername() != null) {
            currentUser.setUsername(data.getUsername());
        }
        return usuarioRepository.save(currentUser);
    }

    public void updateUserPassword(String currentUsername, UpdatePasswordRequest data, PasswordEncoder passwordEncoder) throws Exception {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new Exception("Utilizador não encontrado."));
        if (!passwordEncoder.matches(data.getCurrentPassword(), currentUser.getPassword())) {
            throw new Exception("A senha atual está incorreta.");
        }
        String novaSenhaCriptografada = passwordEncoder.encode(data.getNewPassword());
        currentUser.setPassword(novaSenhaCriptografada);
        usuarioRepository.save(currentUser);
    }

    public Usuario updateUserAvatar(String currentUsername, String avatarFileName) throws Exception {
        Usuario currentUser = (Usuario) this.loadUserByUsername(currentUsername);
        currentUser.setChosenAvatar(avatarFileName);
        return usuarioRepository.save(currentUser);
    }

    @Transactional
    public void deleteUser(String currentUsername) {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + currentUsername));

        // 1. Exclui todas as avaliações associadas a este utilizador
        avaliacaoRepository.deleteByUsuario(currentUser);

        // 2. Exclui o próprio utilizador
        usuarioRepository.delete(currentUser);
    }
}