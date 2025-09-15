package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.controller.UpdatePasswordRequest;
import com.meidesaqua.meidesaqua_backend.controller.UpdateProfileRequest;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.AvaliacaoRepository;
import com.meidesaqua.meidesaqua_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.meidesaqua.meidesaqua_backend.exception.EmailAlreadyExistsException;
import com.meidesaqua.meidesaqua_backend.exception.UsernameAlreadyExistsException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ProfanityFilterService profanityFilterService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + username));


    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario, PasswordEncoder passwordEncoder) throws Exception {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Usuário já cadastrado, use outro e tente novamente.");
        }

        // ALTERAÇÃO AQUI: Verifica o e-mail
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado, use outro e tente novamente.");
        }

        if (profanityFilterService.contemPalavrao(usuario.getUsername())) {
            throw new Exception("O nome de utilizador contém palavras impróprias.");
        }


        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCriptografada);

        String token = UUID.randomUUID().toString();
        usuario.setConfirmationToken(token);
        usuario.setEnabled(false);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        emailService.sendConfirmationEmail(usuarioSalvo.getEmail(), usuarioSalvo.getConfirmationToken());

        return usuarioSalvo;
    }

    @Transactional
    public void confirmUserAccount(String confirmationToken) throws Exception {
        Usuario usuario = usuarioRepository.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new Exception("Token de confirmação inválido ou não encontrado."));

        usuario.setEnabled(true);
        usuario.setConfirmationToken(null);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void confirmEmailChange(String token) throws Exception {
        Usuario usuario = usuarioRepository.findByEmailChangeToken(token)
                .orElseThrow(() -> new Exception("Token de alteração de e-mail inválido ou não encontrado."));

        usuario.setEmail(usuario.getUnconfirmedEmail());
        usuario.setUnconfirmedEmail(null);
        // CORREÇÃO: Limpa o token para que ele não possa ser reutilizado
        usuario.setEmailChangeToken(null);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void forgotPassword(String userEmail) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new Exception("Não foi encontrado um utilizador com o e-mail fornecido."));

        String token = UUID.randomUUID().toString();
        usuario.setResetPasswordToken(token);
        usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));

        usuarioRepository.save(usuario);
        emailService.sendPasswordResetEmail(usuario.getEmail(), token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        Usuario usuario = usuarioRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new Exception("Token de redefinição de senha inválido ou expirado."));

        if (usuario.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new Exception("Token de redefinição de senha expirado.");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiry(null);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario updateUserProfile(String currentUsername, UpdateProfileRequest data) throws Exception {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new Exception("Utilizador não encontrado."));

        if (data.getNomeCompleto() != null) {
            currentUser.setNomeCompleto(data.getNomeCompleto());
        }
        if (data.getUsername() != null) {
            currentUser.setUsername(data.getUsername());
        }

        if (data.getEmail() != null && !data.getEmail().equalsIgnoreCase(currentUser.getEmail())) {
            if (usuarioRepository.findByEmail(data.getEmail()).isPresent()) {
                throw new Exception("O novo e-mail já está em uso por outra conta.");
            }

            String token = UUID.randomUUID().toString();
            currentUser.setUnconfirmedEmail(data.getEmail());
            currentUser.setEmailChangeToken(token);

            emailService.sendEmailChangeConfirmationEmail(data.getEmail(), token);
        }

        return usuarioRepository.save(currentUser);
    }

    @Transactional
    public void updateUserPassword(String username, UpdatePasswordRequest request) throws Exception {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new Exception("A senha atual está incorreta.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(request.getNewPassword());
        currentUser.setPassword(novaSenhaCriptografada);

        usuarioRepository.save(currentUser);
    }

    @Transactional
    public void deleteUser(String currentUsername) {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + currentUsername));

        avaliacaoRepository.deleteByUsuario(currentUser);
        usuarioRepository.delete(currentUser);
    }
}