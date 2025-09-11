package com.meidesaqua.meidesaqua_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

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

    @Column(name = "logoUrl", columnDefinition = "TEXT")
    private String logoUrl;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemProduto> produtosImg = new ArrayList<>();
}