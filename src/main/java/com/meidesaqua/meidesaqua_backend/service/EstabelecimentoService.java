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
        // Verifica se o CNPJ já existe (lógica que adicionámos)
        if (estabelecimento.getCnpj() != null && estabelecimentoRepository.findByCnpj(estabelecimento.getCnpj()).isPresent()) {
            throw new Exception("CNPJ já cadastrado no sistema.");
        }
        return estabelecimentoRepository.save(estabelecimento);
    }
}