package com.meidesaqua.meidesaqua_backend.DTO;

import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.entity.ImagemProduto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EstabelecimentoRequestDTO {

    // Dados de texto do estabelecimento
    private String categoria;
    private String contatoEstabelecimento;
    private String cnpj;
    private String nomeFantasia;
    private String emailEstabelecimento;
    private String endereco;
    private String descricao;
    private String descricaoDiferencial;
    private String website;
    private String instagram;
    private Boolean ativo;

    // Adicione estes dois campos para receber as imagens em Base64
    private String logoBase64;
    private List<String> produtosImgBase64;

    // Metodo para converter este DTO para a sua entidade principal
    public Estabelecimento toEntity() {
        Estabelecimento entity = new Estabelecimento();
        entity.setCategoria(this.categoria);
        entity.setContatoEstabelecimento(this.contatoEstabelecimento);
        entity.setCnpj(this.cnpj);
        entity.setNomeFantasia(this.nomeFantasia);
        entity.setEmailEstabelecimento(this.emailEstabelecimento);
        entity.setEndereco(this.endereco);
        entity.setDescricao(this.descricao);
        entity.setDescricaoDiferencial(this.descricaoDiferencial);
        entity.setWebsite(this.website);
        entity.setInstagram(this.instagram);
        entity.setAtivo(this.ativo);
        // Nota: Os campos de imagem não são convertidos aqui,
        // pois a lógica de salvamento e atribuição de URL é feita no Service.
        return entity;
    }
}