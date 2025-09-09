package com.meidesaqua.meidesaqua_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "avaliacoes")
@Data
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avaliacoes_id")
    private Integer avaliacoesId;

    private String comentario;

    @Column(nullable = false)
    private Double nota;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // CORREÇÃO: Permite que o backend RECEBA o estabelecimento do frontend,
    // mas não o envie de volta em respostas, evitando loops de serialização.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;
}

