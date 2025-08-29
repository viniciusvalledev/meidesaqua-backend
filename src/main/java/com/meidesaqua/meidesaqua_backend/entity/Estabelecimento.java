// Local: src/main/java/com/meidesaqua/meidesaquabackend/entity/Estabelecimento.java
package com.meidesaqua.meidesaqua_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estabelecimentos")
@Data
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estabelecimento_id")
    private Integer estabelecimentoId;

    // Mapeamento direto das colunas para os atributos
    private String categoria;
    private String contato_estabelecimento;
    private String cnpj;
    private String nome_fantasia;
    private String email_estabelecimento;
    private String endereco;
    private String descricao;
    private String descricao_diferencial;
    private String especialidade;
    private String tags_invisiveis;
    private String coordenadas;
    private String website;
    private String instagram;
    private Boolean ativo;
}