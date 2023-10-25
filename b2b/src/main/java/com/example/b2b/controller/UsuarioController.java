package com.example.b2b.controller;

import com.example.b2b.dtos.usuario.RegisterRequestDTO;
import com.example.b2b.dtos.usuario.RegisterResponseDTO;
import com.example.b2b.entity.usuario.Usuario;
import com.example.b2b.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService = new UsuarioService();

    // http://localhost:8080/usuarios
    @GetMapping
    public ResponseEntity<List<RegisterResponseDTO>> getUsuarios() {
        List<Usuario> listaUsuarios = usuarioService.getTodosUsuarios();
        if (listaUsuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<RegisterResponseDTO> listaUsuariosResponse = usuarioService.convertListaResponseDTO(listaUsuarios);
        return ResponseEntity.status(200).body(listaUsuariosResponse);
    }

    // http://localhost:8080/usuarios/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<RegisterResponseDTO> getUsuarioPorCnpj(@PathVariable String cnpj) {
        Usuario usuarioCnpj = usuarioService.getUsuarioPorCnpj(cnpj);

        if (usuarioCnpj == null) {
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(usuarioCnpj.getNome(), usuarioCnpj.getCnpj(), usuarioCnpj.getDataDeCriacao(), usuarioCnpj.getEmail(), usuarioCnpj.getTipoUsuario().toString());
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/usuarios/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<RegisterResponseDTO> editarUsuarioPorCnpj(@RequestBody @Valid RegisterRequestDTO usuario, @PathVariable String cnpj) {
        Usuario resposta = usuarioService.editarUsuarioPorCnpj(usuario, cnpj);
        RegisterResponseDTO respostaDTO = new RegisterResponseDTO(resposta.getNome(), resposta.getCnpj(), resposta.getDataDeCriacao(), resposta.getEmail(), resposta.getTipoUsuario().toString());
        return ResponseEntity.status(200).body(respostaDTO);
    }

    // http://localhost:8080/usuarios/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Void resposta = usuarioService.deletarUsuarioPorCnpj(cnpj);
        return ResponseEntity.status(200).build();
    }
    // http://localhost:8080/usuarios/ordenado
    @GetMapping("/ordenado")
    public ResponseEntity<List<RegisterResponseDTO>> getUsuariosOrdenadoPorData(){
        List<Usuario> listaUsuariosOrdenado = usuarioService.getListaOrdenadaPorData();
        if(listaUsuariosOrdenado.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        List<RegisterResponseDTO> listaUsuariosResponse = usuarioService.convertListaResponseDTO(listaUsuariosOrdenado);
        return ResponseEntity.status(200).body(listaUsuariosResponse);
    }

    // http://localhost:8080/usuarios/ordenado/{DateTime}
    @GetMapping("/ordenado/{data}")
    public ResponseEntity<RegisterResponseDTO> getUsuarioPorData(@PathVariable LocalDateTime data){
        Usuario usuarioData = usuarioService.getUsuarioPorData(data);
        if(usuarioData == null){
            return ResponseEntity.status(204).build();
        }
        RegisterResponseDTO resposta = new RegisterResponseDTO(usuarioData.getNome(), usuarioData.getCnpj(), usuarioData.getDataDeCriacao(), usuarioData.getEmail(), usuarioData.getTipoUsuario().toString());

        return ResponseEntity.status(200).body(resposta);
    }

//    @GetMapping("/downloadCSV")
//    public ResponseEntity<Object> downloadCSV() {
//        try {
//            // Gerar o conteúdo do CSV
//            StringBuilder csvContent = new StringBuilder();
//            csvContent.append("ID, Nome, Email, CNPJ, Data de Criação, Tipo de Usuário\n");
//
//            List<Usuario> listaUsuariosOrdenado = usuarioService.getListaOrdenadaPorData();
//            for (Usuario usuario : listaUsuariosOrdenado) {
//                csvContent.append(String.format("%s, %s, %s, %s, %s, %s\n",
//                        usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCnpj(),
//                        usuario.getDataDeCriacao().toString(), usuario.getTipoUsuario()));
//            }
//
//            // Salvar o arquivo no servidor
//            String filePath = "caminho/para/sua/pasta/arquivo/usuarios_ordenados.csv";  // Substitua pelo caminho correto
//            FileWriter fileWriter = new FileWriter(filePath);
//            fileWriter.write(csvContent.toString());
//            fileWriter.close();
//
//            // Configurar cabeçalhos para o download
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios_ordenados.csv");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.parseMediaType("application/csv"))
//                    .body("Arquivo CSV gerado e salvo em: " + filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/downloadCSV")
    public ResponseEntity<UrlResource> downloadCSV() throws IOException {
        String caminhoDoArquivo = usuarioService.gerarEGravarArquivoCSVB(usuarioService.getListaOrdenadaPorData());

        try {
            UrlResource resource = new UrlResource("file:" + caminhoDoArquivo);

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }



}




