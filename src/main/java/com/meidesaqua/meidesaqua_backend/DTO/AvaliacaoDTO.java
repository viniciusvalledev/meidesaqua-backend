package com.meidesaqua.meidesaqua_backend.DTO;

import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import lombok.Data;

@Data
public class AvaliacaoDTO {

    private Integer avaliacoesId;
    private String comentario;
    private Double nota;
    private UserDTO usuario; // Em vez da entidade Usuario, usamos o UserDTO

    // Construtor que converte a entidade para DTO
    public AvaliacaoDTO(Avaliacao avaliacao) {
        this.avaliacoesId = avaliacao.getAvaliacoesId();
        this.comentario = avaliacao.getComentario();
        this.nota = avaliacao.getNota();
        // Aqui está a mágica: convertemos a entidade Usuario para UserDTO,
        // garantindo que nenhuma senha ou dado sensível seja exposto.
        this.usuario = new UserDTO(avaliacao.getUsuario());
    }
}