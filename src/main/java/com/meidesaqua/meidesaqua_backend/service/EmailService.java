package com.meidesaqua.meidesaqua_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Redefinição de Senha - Meidesaqua");
        message.setText("Para redefinir sua senha, use o seguinte token na nossa página de redefinição.\n\n"
                + "Seu token é: " + token + "\n\n"
                + "Se você não solicitou esta alteração, por favor ignore este e-mail.");
        mailSender.send(message);
    }
}