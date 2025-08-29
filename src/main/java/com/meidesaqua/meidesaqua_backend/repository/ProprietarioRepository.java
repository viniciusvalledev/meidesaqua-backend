package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // Importe a classe Optional

public interface ProprietarioRepository extends JpaRepository<Proprietario, Integer> {

    List<Proprietario> findByNomeCompletoContainingIgnoreCase(String nome);

    // --- NOVO MÉTODO PARA BUSCAR POR CPF ---
    Optional<Proprietario> findByCpf(String cpf);
}