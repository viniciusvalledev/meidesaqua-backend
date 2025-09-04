package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional; // Importe a classe Optional

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.estabelecimento.estabelecimentoId = :estabelecimentoId")
    Double findAverageNotaByEstabelecimentoId(@Param("estabelecimentoId") Integer estabelecimentoId);

    // Metodo para saber se o usuário ja avaliou o estabelecimento
    Optional<Avaliacao> findByUsuarioUsuarioIdAndEstabelecimentoEstabelecimentoId(Integer usuarioId, Integer estabelecimentoId);

    // Dentro da interface AvaliacaoRepository
    List<Avaliacao> findByEstabelecimentoEstabelecimentoId(Integer estabelecimentoId);

    // METODO PARA EXCLUIR TODAS AS AVALIAÇÕES DE UM UTILIZADOR
    void deleteByUsuario(Usuario usuario);
}