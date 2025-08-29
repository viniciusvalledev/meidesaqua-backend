// Local: src/main/java/com/meidesaqua/meidesaquabackend/entity/Proprietario.java
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

    private String nome_completo;
    private String cpf;
    private String contato_proprietario;
    private String email_proprietario;

    @OneToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;
}