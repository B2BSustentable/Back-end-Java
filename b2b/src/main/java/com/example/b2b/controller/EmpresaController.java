package com.example.b2b.controller;

import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.dtos.empresa.UpdateRequestDTO;
import com.example.b2b.dtos.empresa.UpdateResponseDTO;
import com.example.b2b.dtos.produto.ProdutoResponseDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.services.EmpresaService;
import com.example.b2b.services.ResponsavelService;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ResponsavelService responsavelService;

    @Autowired
    private ProdutoController produtoController;

    @Autowired
    private CatalogoController catalogoController;

    // http://localhost:8080/empresas
    @GetMapping
    public ResponseEntity<List<RegisterResponseDTO>> getEmpresa() {
        List<Empresa> listaEmpresas = empresaService.getTodasEmpresas();
        if (listaEmpresas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<RegisterResponseDTO> listaEmpresasResponse = empresaService.convertListaResponseDTO(listaEmpresas);
        return ResponseEntity.status(200).body(listaEmpresasResponse);
    }

    // http://localhost:8080/empresas/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorCnpj(@PathVariable String cnpj) {
        Empresa empresaCnpj = empresaService.getEmpresaPorCnpj(cnpj);

        if (empresaCnpj == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaCnpj.getNomeEmpresa(), empresaCnpj.getCnpj(), empresaCnpj.getDataDeCriacao(), empresaCnpj.getEmail(), empresaCnpj.getTipoPlanos(), empresaCnpj.getDescricao(), empresaCnpj.getPhoto(), empresaCnpj.getPhotoCapa(),empresaCnpj.getEndereco());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/empresas
    @GetMapping("/getEmpresaEmail/{email}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorEmail(@PathVariable String email) {
        Empresa empresaEmail = empresaService.getEmpresaPorEmail(email);

        if (empresaEmail == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaEmail.getNomeEmpresa(), empresaEmail.getCnpj(), empresaEmail.getDataDeCriacao(), empresaEmail.getEmail(), empresaEmail.getTipoPlanos(), empresaEmail.getDescricao(), empresaEmail.getPhoto(), empresaEmail.getPhotoCapa(), empresaEmail.getEndereco());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/empresas/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<UpdateResponseDTO> editarEmpresaPorCnpj(MultipartFile foto, MultipartFile fotoCapa,UpdateRequestDTO empresa, @PathVariable String cnpj) {
        Empresa resposta = empresaService.editarEmpresaPorCnpj(foto, fotoCapa, empresa, cnpj);
        UpdateResponseDTO respostaDTO = new UpdateResponseDTO(
                resposta.getNomeEmpresa(),
                resposta.getEmail(),
                resposta.getDescricao(),
                resposta.getPhoto(),
                resposta.getPhotoCapa(),
                resposta.getEndereco()
        );
        return ResponseEntity.status(200).body(respostaDTO);
    }

    // http://localhost:8080/empresas/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity deletarEmpresaPorCnpj(@PathVariable String cnpj) {
        Void resposta = empresaService.deletarEmpresaPorCnpj(cnpj);
        return ResponseEntity.status(200).build();
    }
    // http://localhost:8080/empresas/ordenado
    @GetMapping("/ordenado")
    public ResponseEntity<List<ResponsavelRegisterResponseDTO>> getResponsavelOrdenadoPorData(){
        List<Responsavel> listaResponsavelOrdenado = responsavelService.getListaOrdenadaPorData();
        if(listaResponsavelOrdenado.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<ResponsavelRegisterResponseDTO> listaResponsavelResponse = responsavelService.convertListaResponseDTO(listaResponsavelOrdenado);
        return ResponseEntity.status(200).body(listaResponsavelResponse);
    }

    // http://localhost:8080/empresas/ordenado/{DateTime}
    @GetMapping("/ordenado/{data}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorData(@PathVariable LocalDateTime data){
        Empresa empresaData = empresaService.getEmpresaPorData(data);
        if(empresaData == null){
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaData.getNomeEmpresa(), empresaData.getCnpj(), empresaData.getDataDeCriacao(), empresaData.getEmail(), empresaData.getTipoPlanos(), empresaData.getDescricao(), empresaData.getPhoto(), empresaData.getPhotoCapa(), empresaData.getEndereco());

        return ResponseEntity.status(200).body(resposta);
    }

    @GetMapping("/downloadCSV")
    public ResponseEntity downloadCSV() throws IOException {
        try {
            empresaService.gerarEGravarArquivoCSV(this.getResponsavelOrdenadoPorData().getBody(), "responsaveis");
            // Ler o conteúdo do arquivo CSV
            File csvFile = new File("responsaveis.csv");
            String csvContent = new String(FileCopyUtils.copyToByteArray(csvFile), StandardCharsets.UTF_8);

            // Configurar a resposta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=responsaveis.csv");

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

    @GetMapping("/downloadTXT")
    public ResponseEntity downloadTXT(List<Catalogo> data) throws IOException {
        try {
            List<ProdutoResponseDTO> catalogo = (List<ProdutoResponseDTO>) produtoController.getTodosProdutos();
            empresaService.gravarArquivoTXT(catalogo, "catalogo");

            // Ler o conteúdo do arquivo CSV
            File txtFile = new File("catalogo.txt");
            String txtContent = new String(FileCopyUtils.copyToByteArray(txtFile), StandardCharsets.UTF_8);

            // Configurar a resposta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"catalogo.txt");

            // Definir o tipo de mídia da resposta
            MediaType mediaType = MediaType.parseMediaType("text/csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(txtContent.length())
                    .contentType(mediaType)
                    .body(new ByteArrayResource(txtContent.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<List<Produto>> importarTxt(MultipartFile arquivo, @PathVariable Integer id) {

        Empresa resposta = empresaService.importarTxtPorId(arquivo, id);

        List<Produto> catalogo = (List<Produto>) catalogoController.getCatalogoPorIdEmpresa(String.valueOf(id));

        return ResponseEntity.status(200).body(catalogo);
    }
}




