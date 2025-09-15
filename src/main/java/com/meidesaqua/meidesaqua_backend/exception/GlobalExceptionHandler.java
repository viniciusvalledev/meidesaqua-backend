// meidesaqua-backend/src/main/java/com/meidesaqua/meidesaqua_backend/exception/GlobalExceptionHandler.java

package com.meidesaqua.meidesaqua_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // --- ALTERAÇÃO AQUI ---
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage()); // Coloca a mensagem da exceção dentro do JSON
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // --- ALTERAÇÃO AQUI ---
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage()); // Coloca a mensagem da exceção dentro do JSON
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // (Opcional, mas recomendado) Ajuste o handler genérico também
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Ocorreu um erro inesperado: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}