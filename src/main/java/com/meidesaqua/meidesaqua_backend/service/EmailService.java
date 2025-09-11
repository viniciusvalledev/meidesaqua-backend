package com.meidesaqua.meidesaqua_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmação de Cadastro - Meidesaqua");

        // IMPORTANTE: Altere "http://localhost:3000" para o endereço do seu frontend
        String confirmationUrl = "http://localhost:3000/confirmar-conta?token=" + token;

        message.setText("Obrigado por se cadastrar! Por favor, clique no link abaixo para ativar sua conta:\n\n"
                + confirmationUrl + "\n\n"
                + "Se você não se cadastrou em nosso site, por favor ignore este e-mail.");

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Redefinição de Senha - Meidesaqua");
        message.setText("Para redefinir sua senha, use o seguinte token na nossa página de redefinição.\n\n"
                + "Seu token é: " + token + "\n\n"
                + "Se você não solicitou esta alteração, por favor ignore este e-mail.");
        mailSender.send(message);
    }
    // Metodo para enviar email quando for alterar o e-mail
    public void sendEmailChangeConfirmationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmação de Alteração de E-mail - Meidesaqua");

        String confirmationUrl = "http://localhost:3000/confirmar-novo-email?token=" + token;

        message.setText("Recebemos um pedido para alterar o e-mail da sua conta para este endereço.\n\n"
                + "Por favor, clique no link abaixo para confirmar a alteração:\n"
                + confirmationUrl + "\n\n"
                + "Se você não solicitou esta alteração, por favor ignore este e-mail.");

        mailSender.send(message);
    }
}