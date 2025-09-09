package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.DTO.AvaliacaoDTO;
import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.AvaliacaoRepository;
import com.meidesaqua.meidesaqua_backend.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // Injeção de dependência para o repositório de estabelecimentos
    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    public Avaliacao submeterAvaliacao(Avaliacao avaliacao, Usuario usuarioLogado) throws Exception {
        // Validação 1: Garantir que a nota está entre 1 e 5
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new IllegalArgumentException("A nota da avaliação deve estar entre 1 e 5.");
        }

        // Validação 2: Verificar se o ID do estabelecimento foi fornecido
        if (avaliacao.getEstabelecimento() == null || avaliacao.getEstabelecimento().getEstabelecimentoId() == null) {
            throw new IllegalArgumentException("O ID do estabelecimento é obrigatório.");
        }

        Integer estabelecimentoId = avaliacao.getEstabelecimento().getEstabelecimentoId();

        // Validação 3: (Reativada) Verificar se o usuário já avaliou este estabelecimento
        if (avaliacaoRepository.findByUsuarioUsuarioIdAndEstabelecimentoEstabelecimentoId(usuarioLogado.getUsuarioId(), estabelecimentoId).isPresent()) {
            throw new IllegalStateException("Este utilizador já avaliou este estabelecimento.");
        }

        // LÓGICA PRINCIPAL: Busca a entidade Estabelecimento completa pelo ID
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new NoSuchElementException("Estabelecimento não encontrado com o ID: " + estabelecimentoId));

        // Associa as entidades "reais" (gerenciadas pelo JPA) à avaliação
        avaliacao.setEstabelecimento(estabelecimento);
        avaliacao.setUsuario(usuarioLogado);

        // Salva a avaliação com as relações corretas
        return avaliacaoRepository.save(avaliacao);
    }

    public Double calcularMediaPorEstabelecimento(Integer estabelecimentoId) {
        return avaliacaoRepository.findAverageNotaByEstabelecimentoId(estabelecimentoId);
    }

    public List<Avaliacao> listarPorEstabelecimento(Integer estabelecimentoId) {
        return avaliacaoRepository.findByEstabelecimentoEstabelecimentoId(estabelecimentoId);
    }

    public List<AvaliacaoDTO> listarPorEstabelecimentoDTO(Integer estabelecimentoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByEstabelecimentoEstabelecimentoId(estabelecimentoId);
        return avaliacoes.stream()
                .map(AvaliacaoDTO::new)
                .collect(Collectors.toList());
    }
}

