// Local: src/main/java/com/meidesaqua/meidesaquabackend/service/ProprietarioService.java
package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.entity.Proprietario;
import com.meidesaqua.meidesaqua_backend.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    public Proprietario cadastrarProprietario(Proprietario proprietario) throws Exception {
        // Regra de Negócio: Verifica se o CPF já está cadastrado
        if (proprietario.getCpf() != null && proprietarioRepository.findByCpf(proprietario.getCpf()).isPresent()) {
            throw new Exception("CPF já cadastrado no sistema.");
        }
        return proprietarioRepository.save(proprietario);
    }
}