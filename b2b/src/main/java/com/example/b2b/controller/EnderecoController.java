package com.example.b2b.controller;

import com.example.b2b.dtos.endereco.EnderecoRequestDTO;
import com.example.b2b.dtos.endereco.EnderecoResponseDTO;
import com.example.b2b.entity.endereco.Endereco;
import com.example.b2b.services.EnderecoService;
import com.example.b2b.util.Operacao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;




    @PostMapping("/{uIdEmpresa}")
    public ResponseEntity<EnderecoResponseDTO> cadastrarEndereco(@RequestBody @Valid EnderecoRequestDTO endereco, @PathVariable String uIdEmpresa) throws Throwable {
        Endereco enderecoEntity = enderecoService.cadastrarEndereco(endereco, uIdEmpresa);
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

    @PostMapping("/enfileirar-operacao")
    public ResponseEntity enfileirarOperacao(@RequestBody @Valid EnderecoRequestDTO endereco,
                                             @RequestParam Operacao operacao,
                                             @RequestParam(required = false) String uIdEmpresa,
                                             @RequestParam(required = false) String uIdEndereco) {
        enderecoService.enfileirarOperacaoEndereco(operacao, endereco, uIdEmpresa, uIdEndereco);
        return ResponseEntity.status(202).build();  // 202 Accepted
    }

    @GetMapping("/processar-fila")
    public ResponseEntity processarFila() {
        Endereco endereco = enderecoService.processarFila();
        if (endereco != null) {
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @DeleteMapping("/{uId}")
    public ResponseEntity deletarEndereco(@PathVariable String uId){
        enderecoService.deletarEndereco(uId);
        return ResponseEntity.status(200).build();
    }

}
