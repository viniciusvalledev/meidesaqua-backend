package com.meidesaqua.meidesaqua_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF, essencial para testes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // <-- Permite qualquer requisiçao
                );
        return http.build();
    }
}