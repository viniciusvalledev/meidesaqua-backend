package com.meidesaqua.meidesaqua_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Rotas Públicas (não precisam de login)
                        .requestMatchers("/api/auth/**").permitAll() // Cadastro e Login
                        .requestMatchers(HttpMethod.GET, "/api/estabelecimentos/**").permitAll() // Todas as buscas de estabelecimentos
                        .requestMatchers(HttpMethod.GET, "/api/proprietarios/**").permitAll() // Todas as buscas de proprietários

                        // Rotas Protegidas (precisam de login)
                        .anyRequest().authenticated() // Qualquer outra requisição (como POST em /api/avaliacoes) exige login
                )
                .httpBasic(withDefaults()); // Habilita a autenticação
        return http.build();
    }
}