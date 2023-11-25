package com.example.b2b.services;

import com.example.b2b.dtos.responsavel.ResponsavelRegisterRequestDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    @Autowired
    private EmpresaService empresaService;

    public List<Responsavel> getResponsaveis() {
        return responsavelRepository.findAll();
    }

    public Responsavel cadastrarResponsavel(ResponsavelRegisterRequestDTO data, String idEmpresa) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(data.emailResponsavel());

        if (empresaService.getEmpresaCadastrada().getUIdEmpresa().equals(idEmpresa)) {
            throw new RuntimeException("Empresa não é compatível com a empresa logada");
        }

        if (responsavelExistente.isPresent()) {
            throw new RuntimeException("Responsavel já cadastrado");
        }

        Responsavel responsavel = new Responsavel(data);
        responsavel.setEmpresa(empresaService.getEmpresaCadastrada());

        return responsavelRepository.save(responsavel);
    }

    public Responsavel getResponsavelPorUId(String id) {
        Optional<Responsavel> responsavel = responsavelRepository.findByUIdResponsavel(id);

        if (responsavel.isPresent()) {
            return responsavel.get();
        } else {
            throw new RuntimeException("Responsavel não encontrado");
        }
    }

    public Responsavel editarResponsavelPorEmail(String email, ResponsavelRegisterRequestDTO data) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(email);

        if (responsavelExistente.isPresent()) {
            Responsavel responsavel = responsavelExistente.get();
            responsavel.setNomeResponsavel(data.nomeResponsavel());
            responsavel.setSobrenomeResponsavel(data.sobrenomeResponsavel());
            responsavel.setEmailResponsavel(data.emailResponsavel());
            responsavel.setSenhaResponsavel(data.senhaResponsavel());
            responsavel.setPhotoResponsavel(data.photoResponsavel());

            return responsavelRepository.save(responsavel);
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

    public Responsavel login(ResponsavelRegisterRequestDTO data) {
        Optional<Responsavel> responsavelExistente = responsavelRepository.findByEmailResponsavel(data.emailResponsavel());

        if (responsavelExistente.isPresent()) {
            Responsavel responsavel = responsavelExistente.get();
            if (responsavel.getSenhaResponsavel().equals(data.senhaResponsavel())) {
                return responsavel;
            } else {
                throw new RuntimeException("Senha incorreta");
            }
        } else {
            throw new RuntimeException("Responsavel não encontrado");
        }
    }

    public List<ResponsavelRegisterResponseDTO> convertListaResponseDTO(List<Responsavel> responsaveis) {
        return responsaveis.stream().map(responsavel -> new ResponsavelRegisterResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), false)).collect(Collectors.toList());
    }
}
