package com.example.b2b.controller;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService = new EmpresaService();

    // http://localhost:8080/usuarios
    @GetMapping
    public ResponseEntity<List<RegisterResponseDTO>> getUsuarios() {
        List<Empresa> listaEmpresas = empresaService.getTodosUsuarios();
        if (listaEmpresas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        Collections.shuffle(listaEmpresas);
        List<RegisterResponseDTO> listaUsuariosResponse = empresaService.convertListaResponseDTO(listaEmpresas);
        return ResponseEntity.status(200).body(listaUsuariosResponse);
    }

    // http://localhost:8080/usuarios/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<RegisterResponseDTO> getUsuarioPorCnpj(@PathVariable String cnpj) {
        Empresa empresaCnpj = empresaService.getUsuarioPorCnpj(cnpj);

        if (empresaCnpj == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaCnpj.getNome(), empresaCnpj.getCnpj(), empresaCnpj.getDataDeCriacao(), empresaCnpj.getEmail(), empresaCnpj.getTipoPlanos().toString());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/usuarios/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<RegisterResponseDTO> editarUsuarioPorCnpj(@RequestBody @Valid RegisterRequestDTO usuario, @PathVariable String cnpj) {
        Empresa resposta = empresaService.editarUsuarioPorCnpj(usuario, cnpj);
        RegisterResponseDTO respostaDTO = new RegisterResponseDTO(resposta.getNome(), resposta.getCnpj(), resposta.getDataDeCriacao(), resposta.getEmail(), resposta.getTipoPlanos().toString());
        return ResponseEntity.status(200).body(respostaDTO);
    }

    // http://localhost:8080/usuarios/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Void resposta = empresaService.deletarUsuarioPorCnpj(cnpj);
        return ResponseEntity.status(200).build();
    }
    // http://localhost:8080/usuarios/ordenado
    @GetMapping("/ordenado")
    public ResponseEntity<List<RegisterResponseDTO>> getUsuariosOrdenadoPorData(){
        List<Empresa> listaUsuariosOrdenado = empresaService.getListaOrdenadaPorData();
        if(listaUsuariosOrdenado.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<RegisterResponseDTO> listaUsuariosResponse = empresaService.convertListaResponseDTO(listaUsuariosOrdenado);
        return ResponseEntity.status(200).body(listaUsuariosResponse);
    }

    // http://localhost:8080/usuarios/ordenado/{DateTime}
    @GetMapping("/ordenado/{data}")
    public ResponseEntity<RegisterResponseDTO> getUsuarioPorData(@PathVariable LocalDateTime data){
        Empresa empresaData = empresaService.getUsuarioPorData(data);
        if(empresaData == null){
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaData.getNome(), empresaData.getCnpj(), empresaData.getDataDeCriacao(), empresaData.getEmail(), empresaData.getTipoPlanos().toString());

        return ResponseEntity.status(200).body(resposta);
    }

    @GetMapping("/downloadCSV")
    public ResponseEntity downloadCSV() throws IOException {
        try {
            empresaService.gerarEGravarArquivoCSV(this.getUsuariosOrdenadoPorData().getBody(), "usuarios");
            // Ler o conteúdo do arquivo CSV
            File csvFile = new File("usuarios.csv");
            String csvContent = new String(FileCopyUtils.copyToByteArray(csvFile), StandardCharsets.UTF_8);

            // Configurar a resposta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios.csv");

            // Definir o tipo de mídia da resposta
            MediaType mediaType = MediaType.parseMediaType("text/csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(csvContent.length())
                    .contentType(mediaType)
                    .body(new ByteArrayResource(csvContent.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}




