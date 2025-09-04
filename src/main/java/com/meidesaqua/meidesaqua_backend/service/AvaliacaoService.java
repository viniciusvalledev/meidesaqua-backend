package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.DTO.AvaliacaoDTO; // IMPORTAR O NOVO DTO
import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.entity.Usuario;
import com.meidesaqua.meidesaqua_backend.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors; // IMPORTAR STREAMS

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // (metodo submeterAvaliacao e calcularMediaPorEstabelecimento permanecem iguais)

    public Avaliacao submeterAvaliacao(Avaliacao avaliacao, Usuario usuarioLogado) throws Exception {
        // Define o utilizador da avaliação como o que está logado
        avaliacao.setUsuario(usuarioLogado);

        // Validação 1: Garantir que a nota está entre 1 e 5
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new Exception("A nota da avaliação deve estar entre 1 e 5.");
        }

        // Validação 2: Verificar se o utilizador já avaliou este estabelecimento
        Integer estabelecimentoId = avaliacao.getEstabelecimento().getEstabelecimentoId();
        if (avaliacaoRepository.findByUsuarioUsuarioIdAndEstabelecimentoEstabelecimentoId(usuarioLogado.getUsuarioId(), estabelecimentoId).isPresent()) {
            throw new Exception("Este utilizador já avaliou este estabelecimento.");
        }

        // Se todas as validações passarem, salva a avaliação
        return avaliacaoRepository.save(avaliacao);
    }

    public Double calcularMediaPorEstabelecimento(Integer estabelecimentoId) {
        return avaliacaoRepository.findAverageNotaByEstabelecimentoId(estabelecimentoId);
    }


    // METODO ANTIGO (ainda pode ser útil internamente)
    public List<Avaliacao> listarPorEstabelecimento(Integer estabelecimentoId) {
        return avaliacaoRepository.findByEstabelecimentoEstabelecimentoId(estabelecimentoId);
    }

    // NOVO METODO: Retorna uma lista de DTOs para a API
    public List<AvaliacaoDTO> listarPorEstabelecimentoDTO(Integer estabelecimentoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByEstabelecimentoEstabelecimentoId(estabelecimentoId);

        // Converte cada Avaliacao da lista para um AvaliacaoDTO
        return avaliacoes.stream()
                .map(AvaliacaoDTO::new)
                .collect(Collectors.toList());
    }
}