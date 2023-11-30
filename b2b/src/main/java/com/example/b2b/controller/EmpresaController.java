package com.example.b2b.controller;

import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.dtos.empresa.UpdateRequestDTO;
import com.example.b2b.dtos.empresa.UpdateResponseDTO;
import com.example.b2b.dtos.produto.ProdutoResponseDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.services.EmpresaService;
import com.example.b2b.services.ProdutoService;
import com.example.b2b.services.ResponsavelService;
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
    private ProdutoService produtoService;

    @Autowired
    private CatalogoController catalogoController;

    // http://localhost:8080/empresas
    @GetMapping
    public ResponseEntity<List<RegisterResponseDTO>> getListaEmpresas() {
        List<Empresa> listaEmpresas = empresaService.getTodasEmpresas();
        if (listaEmpresas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<RegisterResponseDTO> listaEmpresasResponse = empresaService.convertListaResponseDTO(listaEmpresas);
        return ResponseEntity.status(200).body(listaEmpresasResponse);
    }

    // http://localhost:8080/empresas
    @GetMapping("{uIdEmpresa}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorUIdEmpresa(@PathVariable String uIdEmpresa) {
        Empresa empresaUIdEmpresa = empresaService.getEmpresaPorUIdEmpresa(uIdEmpresa);
        if (empresaUIdEmpresa == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaUIdEmpresa.getNomeEmpresa(), empresaUIdEmpresa.getCnpj(), empresaUIdEmpresa.getDataDeCriacao(), empresaUIdEmpresa.getEmail(), empresaUIdEmpresa.getTipoPlanos(), empresaUIdEmpresa.getDescricao(), empresaUIdEmpresa.getPhoto(), empresaUIdEmpresa.getEndereco());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/empresas/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorCnpj(@PathVariable String cnpj) {
        Empresa empresaCnpj = empresaService.getEmpresaPorCnpj(cnpj);

        if (empresaCnpj == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaCnpj.getNomeEmpresa(), empresaCnpj.getCnpj(), empresaCnpj.getDataDeCriacao(), empresaCnpj.getEmail(), empresaCnpj.getTipoPlanos(), empresaCnpj.getDescricao(), empresaCnpj.getPhoto(), empresaCnpj.getEndereco());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/empresas
    @GetMapping("/getEmpresaEmail/{email}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorEmail(@PathVariable String email) {
        Empresa empresaEmail = empresaService.getEmpresaPorEmail(email);

        if (empresaEmail == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaEmail.getNomeEmpresa(), empresaEmail.getCnpj(), empresaEmail.getDataDeCriacao(), empresaEmail.getEmail(), empresaEmail.getTipoPlanos(), empresaEmail.getDescricao(), empresaEmail.getPhoto(), empresaEmail.getEndereco());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/empresas/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<UpdateResponseDTO> editarEmpresaPorCnpj(MultipartFile file, @RequestBody UpdateRequestDTO empresa, @PathVariable String cnpj) throws IOException {
        Empresa resposta = empresaService.editarEmpresaPorCnpj(file, empresa, cnpj);
        UpdateResponseDTO respostaDTO = new UpdateResponseDTO(
                resposta.getNomeEmpresa(),
                resposta.getEmail(),
                resposta.getDescricao(),
                resposta.getPhoto(),
                resposta.getEndereco()
        );
        return ResponseEntity.status(200).body(respostaDTO);
    }

    @PutMapping("/editarPlano/{uIdEmpresa}")
    public ResponseEntity<UpdateResponseDTO> editarPlanoEmpresaPorUIdEmpresa(@PathVariable String uIdEmpresa, @PathVariable String tipoPlano){
        Empresa resposta = empresaService.editarPlanoPorIdEmpresa(uIdEmpresa, tipoPlano);
        UpdateResponseDTO respostaDTO = new UpdateResponseDTO(
                resposta.getNomeEmpresa(),
                resposta.getEmail(),
                resposta.getDescricao(),
                resposta.getPhoto(),
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

    @GetMapping("/empresaPorIdProduto/{idProduto}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorIdProduto(@PathVariable String idProduto){
        Empresa empresaIdProduto = empresaService.getEmpresaPorIdProduto(idProduto);
        if(empresaIdProduto == null){
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaIdProduto.getNomeEmpresa(), empresaIdProduto.getCnpj(), empresaIdProduto.getDataDeCriacao(), empresaIdProduto.getEmail(), empresaIdProduto.getTipoPlanos(), empresaIdProduto.getDescricao(), empresaIdProduto.getPhoto(), empresaIdProduto.getEndereco());

        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/empresas/ordenado/{DateTime}
    @GetMapping("/ordenado/{data}")
    public ResponseEntity<RegisterResponseDTO> getEmpresaPorData(@PathVariable LocalDateTime data){
        Empresa empresaData = empresaService.getEmpresaPorData(data);
        if(empresaData == null){
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(empresaData.getNomeEmpresa(), empresaData.getCnpj(), empresaData.getDataDeCriacao(), empresaData.getEmail(), empresaData.getTipoPlanos(), empresaData.getDescricao(), empresaData.getPhoto(), empresaData.getEndereco());

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

    @GetMapping("/downloadTXT/{uIdEmpresa}")
    public ResponseEntity downloadTXT(@PathVariable String uIdEmpresa) throws IOException {
        try {
            List<ProdutoResponseDTO> catalogo = produtoService.convertListaResponseDTO(produtoService.getTodosProdutosPoruIdEmpresa(uIdEmpresa));
            empresaService.gravarArquivoTXT(catalogo, "catalogo");

            // Ler o conteúdo do arquivo CSV
            File txtFile = new File("catalogo");

            if (!txtFile.exists()) {
                txtFile.createNewFile();
            }

            String txtContent = new String(FileCopyUtils.copyToByteArray(txtFile), StandardCharsets.UTF_8);

            // Configurar a resposta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=/catalogo.txt");

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

    @PutMapping("/importarTXTProdutos/{id}/{nomeArq}")
    public ResponseEntity<List<Produto>>  importarTxt(MultipartFile arquivo, @PathVariable String id, @PathVariable String nomeArq) {

        List<Produto> response = empresaService.importarTxtPorId(arquivo, id, nomeArq);

//        List<ProdutoResponseDTO> catalogo = (List<ProdutoResponseDTO>) catalogoController.getCatalogoPorIdEmpresa(String.valueOf(id));

        return ResponseEntity.status(200).body(response);
    }
}




