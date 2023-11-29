package com.example.b2b.controller;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterRequestDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.dtos.responsavel.UpdateResponsavelRequestDTO;
import com.example.b2b.dtos.responsavel.UpdateResponsavelResponseDTO;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.repository.services.ResponsavelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    @Autowired
    private ResponsavelService responsavelService;

    @PostMapping("/login")
    public ResponseEntity<ResponsavelRegisterResponseDTO> login(@RequestBody @Valid AutenticacaoDTO data) {
        Responsavel responsavel = responsavelService.login(data);
        ResponsavelRegisterResponseDTO responsavelResponseDTO = new ResponsavelRegisterResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), true);
        return ResponseEntity.status(200).body(responsavelResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ResponsavelRegisterResponseDTO>> getTodosResponsaveis() {
        List<Responsavel> responsaveis = responsavelService.getResponsaveis();
        if (responsaveis.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        Collections.shuffle(responsaveis);
        List<ResponsavelRegisterResponseDTO> responsaveisResponseDTO = responsavelService.convertListaResponseDTO(responsaveis);
        return ResponseEntity.status(200).body(responsaveisResponseDTO);
    }

    @GetMapping("/{idResponsavel}")
    public ResponseEntity<ResponsavelRegisterResponseDTO> getResponsavelPorUId(@PathVariable String idResponsavel) {
        Responsavel responsavel = responsavelService.getResponsavelPorUId(idResponsavel);
        ResponsavelRegisterResponseDTO responsavelResponseDTO = new ResponsavelRegisterResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), false);
        return ResponseEntity.status(200).body(responsavelResponseDTO);
    }

    @PostMapping("/{idEmpresa}")
    public ResponseEntity<ResponsavelRegisterResponseDTO> cadastrarResponsavel(@RequestBody @Valid ResponsavelRegisterRequestDTO data, @PathVariable String idEmpresa) {
        Responsavel responsavel = responsavelService.cadastrarResponsavel(data, idEmpresa);
        ResponsavelRegisterResponseDTO responsavelResponseDTO = new ResponsavelRegisterResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), false);
        return ResponseEntity.status(201).body(responsavelResponseDTO);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UpdateResponsavelResponseDTO> editarResponsavel(MultipartFile foto, MultipartFile fotoCapa, @PathVariable String email, UpdateResponsavelRequestDTO data) {
        Responsavel responsavel = responsavelService.editarResponsavelPorEmail(foto, fotoCapa, email, data);
        UpdateResponsavelResponseDTO responsavelResponseDTO = new UpdateResponsavelResponseDTO(responsavel.getNomeResponsavel(), responsavel.getSobrenomeResponsavel(), responsavel.getEmailResponsavel(), responsavel.getPhotoResponsavel(), responsavel.getPhotoCapaResponsavel());
        return ResponseEntity.status(200).body(responsavelResponseDTO);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity deletarResponsavel(@PathVariable String email) {
        responsavelService.deletarResponsavelPorEmail(email);
        return ResponseEntity.status(200).build();
    }

}
