package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.entity.Avaliacao;
import com.meidesaqua.meidesaqua_backend.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public Avaliacao submeterAvaliacao(Avaliacao avaliacao) throws Exception {
        // Validação 1: Garantir que a nota está entre 1 e 5
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new Exception("A nota da avaliação deve estar entre 1 e 5.");
        }

        // Validação 2: Verificar se o utilizador já avaliou este estabelecimento
        Integer usuarioId = avaliacao.getUsuario().getUsuarioId();
        Integer estabelecimentoId = avaliacao.getEstabelecimento().getEstabelecimentoId();

        if (avaliacaoRepository.findByUsuarioUsuarioIdAndEstabelecimentoEstabelecimentoId(usuarioId, estabelecimentoId).isPresent()) {
            throw new Exception("Este utilizador já avaliou este estabelecimento.");
        }

        // Se todas as validações passarem, salva a avaliação
        return avaliacaoRepository.save(avaliacao);
    }
}