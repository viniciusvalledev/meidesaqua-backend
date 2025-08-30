package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Integer> {
    // Busca estabelecimentos cujo nome fantasia contenha o texto pesquisado, ignorando maiúsculas/minúsculas
    List<Estabelecimento> findByNomeFantasiaContainingIgnoreCase(String nome);

    Optional<Estabelecimento> findByCnpj(String cnpj);
}