package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.DTO.EstabelecimentoDTO; // ADICIONE ESTE IMPORT
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

    // 1. INJETE O AVALIACAOSERVICE PARA PODERMOS USÁ-LO
    @Autowired
    private AvaliacaoService avaliacaoService;

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
        if (estabelecimento.getCnpj() != null && estabelecimentoRepository.findByCnpj(estabelecimento.getCnpj()).isPresent()) {
            throw new Exception("CNPJ já cadastrado no sistema.");
        }
        return estabelecimentoRepository.save(estabelecimento);
    }

    // Metodo para ativar e desativar os MEIs
    public Estabelecimento alterarStatusAtivo(Integer id, boolean novoStatus) throws Exception {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new Exception("Estabelecimento não encontrado com o ID: " + id));

        estabelecimento.setAtivo(novoStatus);
        return estabelecimentoRepository.save(estabelecimento);
    }
    public Optional<Estabelecimento> buscarPorNomeFantasia(String nome) {
        return estabelecimentoRepository.findByNomeFantasia(nome);
    }

    // 2. METODO DE CONVERSÃO PARA O DTO
    /**
     * Converte uma entidade Estabelecimento para um EstabelecimentoDTO,
     * calculando e incluindo a média das avaliações.
     */
    public EstabelecimentoDTO convertToDto(Estabelecimento estabelecimento) {
        EstabelecimentoDTO dto = new EstabelecimentoDTO();

        // Mapeia os campos da entidade para o DTO
        dto.setEstabelecimentoId(estabelecimento.getEstabelecimentoId());
        dto.setCategoria(estabelecimento.getCategoria());
        dto.setContatoEstabelecimento(estabelecimento.getContatoEstabelecimento());
        dto.setNomeFantasia(estabelecimento.getNomeFantasia());
        dto.setEmailEstabelecimento(estabelecimento.getEmailEstabelecimento());
        dto.setEndereco(estabelecimento.getEndereco());
        dto.setDescricao(estabelecimento.getDescricao());
        dto.setDescricaoDiferencial(estabelecimento.getDescricaoDiferencial());
        dto.setWebsite(estabelecimento.getWebsite());
        dto.setInstagram(estabelecimento.getInstagram());
        dto.setAtivo(estabelecimento.getAtivo());

        // Calcula e define a média das avaliações
        Double media = avaliacaoService.calcularMediaPorEstabelecimento(estabelecimento.getEstabelecimentoId());
        dto.setMedia(media != null ? media : 0.0); // Se não houver avaliações, a média é 0.0

        return dto;
    }
}