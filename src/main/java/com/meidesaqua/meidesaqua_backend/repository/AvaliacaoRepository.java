// Local: src/main/java/com/meidesaqua/meidesaquabackend/repository/AvaliacaoRepository.java
package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {
    // Query customizada para calcular a média de notas de um estabelecimento específico
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.estabelecimento.estabelecimentoId = :estabelecimentoId")
    Double findAverageNotaByEstabelecimentoId(@Param("estabelecimentoId") Integer estabelecimentoId);
}