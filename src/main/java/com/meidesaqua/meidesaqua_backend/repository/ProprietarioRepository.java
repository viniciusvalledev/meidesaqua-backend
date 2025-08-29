// Local: src/main/java/com/meidesaqua/meidesaquabackend/repository/ProprietarioRepository.java
package com.meidesaqua.meidesaqua_backend.repository;

import com.meidesaqua.meidesaqua_backend.entity.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Integer> {
    // Busca proprietários cujo nome completo contenha o texto pesquisado
    List<Proprietario> findByNomeCompletoContainingIgnoreCase(String nome);
}