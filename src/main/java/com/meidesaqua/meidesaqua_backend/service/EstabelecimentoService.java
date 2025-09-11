package com.meidesaqua.meidesaqua_backend.service;

import com.meidesaqua.meidesaqua_backend.DTO.EstabelecimentoDTO;
import com.meidesaqua.meidesaqua_backend.DTO.EstabelecimentoRequestDTO;
import com.meidesaqua.meidesaqua_backend.entity.Estabelecimento;
import com.meidesaqua.meidesaqua_backend.entity.ImagemProduto;
import com.meidesaqua.meidesaqua_backend.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstabelecimentoService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private FileStorageService fileStorageService;

    // --- METODO CORRIGIDO ---
    @Transactional
    public Estabelecimento cadastrarEstabelecimentoComImagensBase64(EstabelecimentoRequestDTO dto) throws Exception {

        if (dto.getCnpj() != null && !dto.getCnpj().isEmpty() && estabelecimentoRepository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new Exception("CNPJ já cadastrado no sistema.");
        }

        // Converte os dados de texto do DTO para a entidade
        Estabelecimento estabelecimento = dto.toEntity();

        // 1. Salva a imagem da logo, se ela foi enviada
        if (dto.getLogoBase64() != null && !dto.getLogoBase64().isEmpty()) {
            String logoUrl = fileStorageService.saveBase64(dto.getLogoBase64());
            estabelecimento.setLogoUrl(logoUrl);
        }

        // 2. Salva as imagens do carrossel, se elas foram enviadas
        if (dto.getProdutosImgBase64() != null && !dto.getProdutosImgBase64().isEmpty()) {
            List<ImagemProduto> imagens = new ArrayList<>();
            for (String base64Image : dto.getProdutosImgBase64()) {
                String imgUrl = fileStorageService.saveBase64(base64Image);
                imagens.add(new ImagemProduto(imgUrl, estabelecimento));
            }
            estabelecimento.setProdutosImg(imagens);
        }

        // 3. Salva o estabelecimento completo no banco de dados
        return estabelecimentoRepository.save(estabelecimento);
    }

    public List<Estabelecimento> listarTodos() {
        return estabelecimentoRepository.findAll();
    }

    public Optional<Estabelecimento> buscarPorId(Integer id) {
        return estabelecimentoRepository.findById(id);
    }

    public List<Estabelecimento> buscarPorNome(String nome) {
        return estabelecimentoRepository.findByNomeFantasiaContainingIgnoreCase(nome);
    }

    public Optional<Estabelecimento> buscarPorNomeFantasia(String nome) {
        return estabelecimentoRepository.findByNomeFantasia(nome);
    }

    public Estabelecimento alterarStatusAtivo(Integer id, boolean novoStatus) throws Exception {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new Exception("Estabelecimento não encontrado com o ID: " + id));

        estabelecimento.setAtivo(novoStatus);
        return estabelecimentoRepository.save(estabelecimento);
    }

    public EstabelecimentoDTO convertToDto(Estabelecimento estabelecimento) {
        EstabelecimentoDTO dto = new EstabelecimentoDTO();

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
        dto.setLogoUrl(estabelecimento.getLogoUrl());

        if (estabelecimento.getProdutosImg() != null) {
            List<String> urls = estabelecimento.getProdutosImg().stream()
                    .map(ImagemProduto::getUrl)
                    .collect(Collectors.toList());
            dto.setProdutosImg(urls);
        }

        Double media = avaliacaoService.calcularMediaPorEstabelecimento(estabelecimento.getEstabelecimentoId());
        dto.setMedia(media != null ? media : 0.0);

        return dto;
    }
}