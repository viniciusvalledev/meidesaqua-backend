package com.meidesaqua.meidesaqua_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "proprietario")
@Data
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proprietario_id")
    private Integer proprietarioId;
    @Column(name = "nome_completo")
    private String nomeCompletoP;
    private String cpf;
    @Column(name = "contato_proprietario")
    private String contatoProprietario;
    @Column(name = "email_proprietario")
    private String emailProprietario;

    @OneToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;
}