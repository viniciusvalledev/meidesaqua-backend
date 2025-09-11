package com.meidesaqua.meidesaqua_backend.DTO;

import lombok.Data;
import java.util.List;

@Data
public class EstabelecimentoDTO {
    private Integer estabelecimentoId;
    private String categoria;
    private String contatoEstabelecimento;
    private String nomeFantasia;
    private String emailEstabelecimento;
    private String endereco;
    private String descricao;
    private String descricaoDiferencial;
    private String website;
    private String instagram;
    private Boolean ativo;
    private String logoUrl;
    private List<String> produtosImg;
    private Double media; // A média será um número (Double)
}