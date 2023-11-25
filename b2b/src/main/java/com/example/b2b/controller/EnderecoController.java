package com.example.b2b.controller;

import com.example.b2b.dtos.endereco.EnderecoRequestDTO;
import com.example.b2b.dtos.endereco.EnderecoResponseDTO;
import com.example.b2b.entity.endereco.Endereco;
import com.example.b2b.services.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> cadastrarEndereco(@RequestBody @Valid EnderecoRequestDTO endereco) throws Throwable {
        Endereco enderecoEntity = enderecoService.cadastrarEndereco(endereco);
        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO(enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getBairro(), enderecoEntity.getCidade(), enderecoEntity.getEstado(), enderecoEntity.getPais(), enderecoEntity.getCep(), enderecoEntity.getLatitude(), enderecoEntity.getLongitude(), enderecoEntity.getEmpresa().getNomeEmpresa(), enderecoEntity.getEmpresa().getCnpj(), enderecoEntity.getEmpresa().getEmail());
        return ResponseEntity.status(201).body(enderecoResponseDTO);
    }

    @GetMapping("/{uId}")
    public ResponseEntity<EnderecoResponseDTO> getEnderecoPorUId(@PathVariable String uId){
        Endereco enderecoEntity = enderecoService.getEnderecoPorUId(uId);
        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO(enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getBairro(), enderecoEntity.getCidade(), enderecoEntity.getEstado(), enderecoEntity.getPais(), enderecoEntity.getCep(), enderecoEntity.getLatitude(), enderecoEntity.getLongitude(), enderecoEntity.getEmpresa().getNomeEmpresa(), enderecoEntity.getEmpresa().getCnpj(), enderecoEntity.getEmpresa().getEmail());
        return ResponseEntity.status(200).body(enderecoResponseDTO);
    }

    @PutMapping("/{uId}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(@PathVariable String uId, @RequestBody @Valid EnderecoRequestDTO body){
        Endereco enderecoEntity = enderecoService.atualizarEndereco(uId, body);

        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO(enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getBairro(), enderecoEntity.getCidade(), enderecoEntity.getEstado(), enderecoEntity.getPais(), enderecoEntity.getCep(), enderecoEntity.getLatitude(), enderecoEntity.getLongitude(), enderecoEntity.getEmpresa().getNomeEmpresa(), enderecoEntity.getEmpresa().getCnpj(), enderecoEntity.getEmpresa().getEmail());

        return ResponseEntity.status(200).body(enderecoResponseDTO);
    }

    @DeleteMapping("/{uId}")
    public ResponseEntity deletarEndereco(@PathVariable String uId){
        enderecoService.deletarEndereco(uId);
        return ResponseEntity.status(200).build();
    }

}
