package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.DTO.AvaliacaoDTO;
import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.AvaliacaoRepository;
import com.meidesaqua.meidesaqua_backend.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ProfanityFilterService profanityFilterService; // <-- Injetando o filtro

    public Avaliacao submeterAvaliacao(Avaliacao avaliacao, Usuario usuarioLogado) throws Exception {
        // Validação 1: Garantir que a nota está entre 1 e 5
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new IllegalArgumentException("A nota da avaliação deve estar entre 1 e 5.");
        }

        // Validação 2: Verificar se o ID do estabelecimento foi fornecido
        if (avaliacao.getEstabelecimento() == null || avaliacao.getEstabelecimento().getEstabelecimentoId() == null) {
            throw new IllegalArgumentException("O ID do estabelecimento é obrigatório.");
        }

        // Validação 3: Filtro de palavrões no comentário
        if (profanityFilterService.contemPalavrao(avaliacao.getComentario())) {
            throw new IllegalArgumentException("O seu comentário contém palavras impróprias.");
        }

        Integer estabelecimentoId = avaliacao.getEstabelecimento().getEstabelecimentoId();

        // Validação 4: Verificar se o usuário já avaliou este estabelecimento
        if (avaliacaoRepository.findByUsuarioUsuarioIdAndEstabelecimentoEstabelecimentoId(usuarioLogado.getUsuarioId(), estabelecimentoId).isPresent()) {
            throw new IllegalStateException("Este utilizador já avaliou este estabelecimento.");
        }

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new NoSuchElementException("Estabelecimento não encontrado com o ID: " + estabelecimentoId));

        avaliacao.setEstabelecimento(estabelecimento);
        avaliacao.setUsuario(usuarioLogado);

        return avaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public Avaliacao atualizarAvaliacao(Integer avaliacaoId, Avaliacao dadosAvaliacao, Usuario usuarioLogado) throws AccessDeniedException {
        Avaliacao avaliacaoExistente = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new NoSuchElementException("Avaliação não encontrada com o ID: " + avaliacaoId));

        if (!Objects.equals(avaliacaoExistente.getUsuario().getUsuarioId(), usuarioLogado.getUsuarioId())) {
            throw new AccessDeniedException("Você não tem permissão para editar esta avaliação.");
        }

        if (dadosAvaliacao.getComentario() != null) {
            // Filtro de palavrões na atualização
            if(profanityFilterService.contemPalavrao(dadosAvaliacao.getComentario())) {
                throw new IllegalArgumentException("O seu comentário contém palavras impróprias.");
            }
            avaliacaoExistente.setComentario(dadosAvaliacao.getComentario());
        }

        if (dadosAvaliacao.getNota() != null) {
            if (dadosAvaliacao.getNota() < 1 || dadosAvaliacao.getNota() > 5) {
                throw new IllegalArgumentException("A nota da avaliação deve estar entre 1 e 5.");
            }
            avaliacaoExistente.setNota(dadosAvaliacao.getNota());
        }

        return avaliacaoRepository.save(avaliacaoExistente);
    }

    @Transactional
    public void excluirAvaliacao(Integer avaliacaoId, Usuario usuarioLogado) throws AccessDeniedException {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new NoSuchElementException("Avaliação não encontrada com o ID: " + avaliacaoId));

        if (!Objects.equals(avaliacao.getUsuario().getUsuarioId(), usuarioLogado.getUsuarioId())) {
            throw new AccessDeniedException("Você não tem permissão para excluir esta avaliação.");
        }

        avaliacaoRepository.delete(avaliacao);
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