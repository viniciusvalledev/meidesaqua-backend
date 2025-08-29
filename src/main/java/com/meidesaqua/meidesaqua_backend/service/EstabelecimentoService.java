// Local: src/main/java/com/meidesaqua/meidesaquabackend/service/EstabelecimentoService.java
package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EstabelecimentoService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    // Metodo para buscar todos os estabelecimentos da base de dados
    public List<Estabelecimento> listarTodos() {
        return estabelecimentoRepository.findAll();
    }

    // Metodo para buscar um único estabelecimento pelo seu ID
    public Optional<Estabelecimento> buscarPorId(Integer id) {
        return estabelecimentoRepository.findById(id);
    }

    // Metodo para buscar estabelecimentos por parte do nome de fantasia
    public List<Estabelecimento> buscarPorNome(String nome) {
        return estabelecimentoRepository.findByNomeFantasiaContainingIgnoreCase(nome);
    }

    // Metodo de cadastro
    public Estabelecimento cadastrarEstabelecimento(Estabelecimento estabelecimento) throws Exception {
        // 1. Pega o CNPJ do estabelecimento que estamos tentando cadastrar
        String cnpj = estabelecimento.getCnpj();

        // 2. Verifica se o CNPJ não é nulo e se já existe no banco
        if (cnpj != null && estabelecimentoRepository.findByCnpj(cnpj).isPresent()) {
            // 3. Se existir, lança um erro com uma mensagem clara
            throw new Exception("CNPJ já cadastrado no sistema.");
        }

        // 4. Se não existir, salva o novo estabelecimento
        return estabelecimentoRepository.save(estabelecimento);
    }
}