package com.example.b2b.services;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterRequestDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.dtos.responsavel.UpdateResponsavelRequestDTO;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.repository.ResponsavelRepository;
import com.example.b2b.util.Lista;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ArquivoService imagemService;

    public List<Responsavel> getResponsaveis() {
        return responsavelRepository.findAll();
    }

    public ResponseEntity<String> cadastrarResponsavel(ResponsavelRegisterRequestDTO data, String idEmpresa) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(data.emailResponsavel());

        if (empresaService.getEmpresaPorId(idEmpresa).getPlano().getQtdNegociantes() <= responsavelRepository.countResponsavelByEmpresa(empresaService.getEmpresaPorId(idEmpresa))) {
            return new ResponseEntity<String>("Limite de negociantes atingido",HttpStatus.CONFLICT );
        }

        if (!empresaService.getEmpresaPorId(idEmpresa).getUIdEmpresa().equals(idEmpresa)) {
            return new ResponseEntity<String>("Empresa não autorizada" ,HttpStatus.UNAUTHORIZED );
        }

        if (empresaService.getEmpresaPorId(idEmpresa).equals(null)) {
            return new ResponseEntity<String>("Empresa não cadastrada", HttpStatus.UNAUTHORIZED);
        }

        if (responsavelExistente.isPresent()) {
            return new ResponseEntity<String>( "Responsavel ja cadastrado",HttpStatus.CONFLICT );
        }

        Responsavel responsavel = new Responsavel(data);
        responsavel.setEmpresa(empresaService.getEmpresaPorId(idEmpresa));
        responsavelRepository.save(responsavel);
        return new ResponseEntity<String>("criado com sucesso", HttpStatus.CREATED);
    }

    public Responsavel getResponsavelPorUId(String id) {
        Optional<Responsavel> responsavel = responsavelRepository.findByuIdResponsavel(id);

        if (responsavel.isPresent()) {
            return responsavel.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsavel não encontrado");
        }
    }

    public List<Responsavel> getTodosResponsaveisPorEmpresa(String idEmpresa) {
        Optional<List> responsaveis = Optional.ofNullable(responsavelRepository.findAllByEmpresa(empresaService.getEmpresaPorId(idEmpresa)));
        if (responsaveis.isPresent()) {
            return responsaveis.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Lista de responsáveis vazia");
        }
    }

    public Responsavel editarResponsavelPorEmail(MultipartFile foto, String email, UpdateResponsavelRequestDTO data) throws IOException {
        Optional<Responsavel> responsavelExistenteOptional = responsavelRepository.findByEmailResponsavel(email);

        if (responsavelExistenteOptional.isPresent()) {
            Responsavel responsavelExistente = responsavelExistenteOptional.get();

            String filePath = "";
            if (foto != null) {
                 filePath = imagemService.uploadImagem(foto);
            }

            // Atualize os campos do responsável existente com os valores do DTO editado
            empresaService.atualizarCampoSeNaoNulo(data.nomeResponsavel(), responsavelExistente::setNomeResponsavel);
            empresaService.atualizarCampoSeNaoNulo(data.sobrenomeResponsavel(), responsavelExistente::setSobrenomeResponsavel);
            empresaService.atualizarCampoSeNaoNulo(data.emailResponsavel(), responsavelExistente::setEmailResponsavel);
            empresaService.atualizarCampoSeNaoNulo(data.senhaResponsavel(), responsavelExistente::setSenhaResponsavel);
            empresaService.atualizarCampoSeNaoNulo(filePath, responsavelExistente::setPhotoResponsavel);

            // Salve o responsável atualizado no banco de dados
            return responsavelRepository.save(responsavelExistente);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsavel não encontrado");
        }
    }

    public Void deletarResponsavelPorEmail(String email) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(email);
        if (responsavelExistente.isPresent()) {
            responsavelRepository.delete(responsavelExistente.get());
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsavel não encontrado");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsavel não encontrado");
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
        return responsaveis.stream().map(responsavel -> new ResponsavelRegisterResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), false, responsavel.getPhotoResponsavel())).collect(Collectors.toList());
    }
}
