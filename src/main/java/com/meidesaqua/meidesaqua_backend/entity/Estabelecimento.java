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
    private String categoria;
    @Column(name = "contato_estabelecimento")
    private String contatoEstabelecimento;
    private String cnpj;
    @Column(name = "nome_fantasia")
    private String nomeFantasia;
    @Column(name = "email_estabelecimento")
    private String emailEstabelecimento;
    private String endereco;
    private String descricao;
    @Column(name = "descricao_diferencial")
    private String descricaoDiferencial;
    private String especialidade;
    @Column(name = "tags_invisiveis")
    private String tagsInvisiveis;
    private String coordenadas;
    private String website;
    private String instagram;
    private Boolean ativo;

    // Mapeamento direto das colunas para os atributos
}