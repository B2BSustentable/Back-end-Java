package com.example.b2b.services;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterRequestDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.repository.ResponsavelRepository;
import com.example.b2b.util.Lista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    @Autowired
    private EmpresaService empresaService;

    private final Path caminhoImagem = Path.of(System.getProperty("user.dir") + "/arquivo"); // projeto

    public List<Responsavel> getResponsaveis() {
        return responsavelRepository.findAll();
    }

    public Responsavel cadastrarResponsavel(ResponsavelRegisterRequestDTO data, String idEmpresa) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(data.emailResponsavel());

        if (empresaService.getEmpresaCadastrada().getPlano().getQtdNegociantes() <= responsavelRepository.countResponsavelByEmpresa(empresaService.getEmpresaCadastrada()).size()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Limite de negociantes atingido");
        }

        if (!empresaService.getEmpresaCadastrada().getUIdEmpresa().equals(idEmpresa)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Empresa não autorizada");
        }

        if (responsavelExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Responsavel já cadastrado");
        }

        Responsavel responsavel = new Responsavel(data);
        responsavel.setEmpresa(empresaService.getEmpresaCadastrada());

        return responsavelRepository.save(responsavel);
    }

    public Responsavel getResponsavelPorUId(String id) {
        Optional<Responsavel> responsavel = responsavelRepository.findByuIdResponsavel(id);

        if (responsavel.isPresent()) {
            return responsavel.get();
        } else {
            throw new RuntimeException("Responsavel não encontrado");
        }
    }

    private String formatarNomeArquivo(String nomeOriginal) {
        return String.format("%s_%s", UUID.randomUUID(), nomeOriginal);
    }


    public Responsavel editarResponsavelPorEmail(MultipartFile foto, String email, ResponsavelRegisterRequestDTO data) {
        Optional<Responsavel> responsavelExistenteOptional = responsavelRepository.findByEmailResponsavel(email);

        if (responsavelExistenteOptional.isPresent()) {
            Responsavel responsavelExistente = responsavelExistenteOptional.get();

            if (!this.caminhoImagem.toFile().exists()) {
                this.caminhoImagem.toFile().mkdir();
            }

            String nomeArquivoFormatado = formatarNomeArquivo(foto.getOriginalFilename());
            String filePath = this.caminhoImagem + "/" + nomeArquivoFormatado;
            File dest = new File(filePath);

            try {
                foto.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao salvar a foto.", e);
            }

            responsavelExistente.setNomeResponsavel(data.nomeResponsavel());
            responsavelExistente.setSobrenomeResponsavel(data.sobrenomeResponsavel());
            responsavelExistente.setEmailResponsavel(data.emailResponsavel());
            responsavelExistente.setSenhaResponsavel(data.senhaResponsavel());
            responsavelExistente.setPhotoResponsavel(data.photoResponsavel());

            return responsavelRepository.save(responsavelExistente);
        } else {
            throw new RuntimeException("Responsavel não encontrado");
        }

    }

    public Void deletarResponsavelPorEmail(String email) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(email);
        if (responsavelExistente.isPresent()) {
            responsavelRepository.delete(responsavelExistente.get());
            return null;
        } else {
            throw new RuntimeException("Responsavel não encontrado");
        }
    }

    public Responsavel login(AutenticacaoDTO data) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(data.email());

        if (responsavelExistente.isPresent()) {
            Responsavel responsavel = responsavelExistente.get();
            if (responsavel.getSenhaResponsavel().equals(data.senha())) {
                return responsavel;
            } else {
                throw new RuntimeException("Senha incorreta");
            }
        } else {
            throw new RuntimeException("Responsavel não encontrado");
        }
    }

    public List<Responsavel> getListaOrdenadaPorData() {
        List<Responsavel> listaDeResponsavel = responsavelRepository.findAll();
        Lista<Responsavel> lista = new Lista<>();
        lista.adicionarTodos(listaDeResponsavel);
        lista.selectionSortByDataDeCriacao();
        return lista.toList();
    }

    public List<ResponsavelRegisterResponseDTO> convertListaResponseDTO(List<Responsavel> responsaveis) {
        return responsaveis.stream().map(responsavel -> new ResponsavelRegisterResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), false)).collect(Collectors.toList());
    }
}
