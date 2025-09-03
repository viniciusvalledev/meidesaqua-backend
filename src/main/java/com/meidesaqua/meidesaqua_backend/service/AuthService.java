package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.controller.UpdateProfileRequest;
import com.meidesaqua.meidesaqua_backend.controller.UpdatePasswordRequest;
import com.meidesaqua.meidesaqua_backend.entity.PasswordResetToken;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.PasswordResetTokenRepository;
import com.meidesaqua.meidesaqua_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    //  METODO: INICIAR REDEFINIÇÃO DE SENHA
    public void createPasswordResetTokenForUser(String email) {
        usuarioRepository.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            PasswordResetToken myToken = new PasswordResetToken(token, user);
            tokenRepository.save(myToken);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });
    }

    // METODO: REDEFINIR A SENHA
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Token inválido."));

        if (isTokenExpired(resetToken)) {
            tokenRepository.delete(resetToken);
            throw new Exception("Token expirado.");
        }

        Usuario user = resetToken.getUsuario();
        String updatedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(updatedPassword);
        usuarioRepository.save(user);
        tokenRepository.delete(resetToken); // Invalida o token após o uso
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().before(Calendar.getInstance().getTime());
    }

    // METODO: DELETAR PERFIL
    public void deleteUser(String username) throws Exception {
        Usuario user = usuarioRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new Exception("Utilizador não encontrado."));
        // Adicional: Lidar com dependências (ex: avaliações) antes de deletar
        usuarioRepository.delete(user);
    }
}