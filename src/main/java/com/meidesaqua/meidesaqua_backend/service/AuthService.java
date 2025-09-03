package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.controller.UpdateProfileRequest;
import com.meidesaqua.meidesaqua_backend.controller.UpdatePasswordRequest;
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

    // METODO: ATUALIZAR PERFIL
    public Usuario updateUserProfile(String currentUsername, UpdateProfileRequest data) throws Exception {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new Exception("Utilizador não encontrado."));

        // Atualiza os campos se eles não forem nulos no pedido
        if (data.getNomeCompleto() != null) {
            currentUser.setNomeCompleto(data.getNomeCompleto());
        }
        if (data.getEmail() != null) {
            // Opcional: Adicionar validação se o novo email já existe
            currentUser.setEmail(data.getEmail());
        }
        if (data.getUsername() != null) {
            // Opcional: Adicionar validação se o novo username já existe
            currentUser.setUsername(data.getUsername());
        }

        return usuarioRepository.save(currentUser);
    }

    // METODO: ATUALIZAR SENHA
    public void updateUserPassword(String currentUsername, UpdatePasswordRequest data, PasswordEncoder passwordEncoder) throws Exception {
        Usuario currentUser = usuarioRepository.findByEmailOrUsername(currentUsername, currentUsername)
                .orElseThrow(() -> new Exception("Utilizador não encontrado."));

        // 1. Verifica se a senha atual fornecida corresponde à senha guardada
        if (!passwordEncoder.matches(data.getCurrentPassword(), currentUser.getPassword())) {
            throw new Exception("A senha atual está incorreta.");
        }

        // 2. Se corresponder, criptografa e guarda a nova senha
        String novaSenhaCriptografada = passwordEncoder.encode(data.getNewPassword());
        currentUser.setPassword(novaSenhaCriptografada);

        usuarioRepository.save(currentUser);
    }

    // NOVO MÉTODO: ATUALIZAR AVATAR
    public Usuario updateUserAvatar(String currentUsername, String avatarFileName) throws Exception {
        // Reutiliza o método loadUserByUsername para encontrar o usuário e já faz o cast para a entidade Usuario
        Usuario currentUser = (Usuario) this.loadUserByUsername(currentUsername);

        // Define o nome do arquivo do avatar escolhido
        currentUser.setChosenAvatar(avatarFileName);

        // Salva a alteração no banco de dados e retorna o usuário atualizado
        return usuarioRepository.save(currentUser);
    }
}