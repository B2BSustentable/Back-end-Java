package com.example.b2b.controller;

import com.example.b2b.dtos.usuario.RegisterDTO;
import com.example.b2b.entity.usuario.Usuario;
import com.example.b2b.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService = new UsuarioService();

    // http://localhost:8080/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        List<Usuario> listaUsuarios = usuarioService.getTodosUsuarios();
        if (listaUsuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(listaUsuarios);
        }
    }

    // http://localhost:8080/usuarios/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<Usuario> getUsuarioPorCnpj(@PathVariable String cnpj) {
        Usuario usuarioCnpj = usuarioService.getUsuarioPorCnpj(cnpj);
        return ResponseEntity.status(200).body(usuarioCnpj);
    }

    // http://localhost:8080/usuarios/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody @Valid RegisterDTO usuario, @PathVariable String cnpj) {
        Usuario resposta = usuarioService.editarUsuarioPorCnpj(usuario, cnpj);
        return ResponseEntity.status(200).body(resposta);
    }

    // http://localhost:8080/usuarios/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Void resposta = usuarioService.deletarUsuarioPorCnpj(cnpj);
        return ResponseEntity.status(200).build();
    }
    // http://localhost:8080/usuarios/ordenado
    @GetMapping("/ordenado")
    public ResponseEntity<List<Usuario>> getUsuariosOrdenadoPorTipo(){
        List<Usuario> listaUsuariosOrdenado = usuarioService.getListaOrdenadaPorData();
        if(listaUsuariosOrdenado.isEmpty()){
            return ResponseEntity.status(204).build();
        }


        return ResponseEntity.ok(listaUsuariosOrdenado);
    }

    // http://localhost:8080/usuarios/ordenado/{DateTime}
    @GetMapping("/ordenado/{data}")
    public ResponseEntity<Usuario> getUsuarioPorData(@PathVariable LocalDate data){
        Usuario usuarioData = usuarioService.getUsuarioPorData(data);
        if(usuarioData == null){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(usuarioData);
    }

    @GetMapping("/downloadCSV")
    public ResponseEntity<byte[]> downloadCSV() {

        StringBuilder csvContent = new StringBuilder("ID, Nome, Email, CNPJ, Data de Criação, Tipo de Usuário\n");

        List<Usuario> listaUsuarios = usuarioService.getListaOrdenadaPorData();

        for (Usuario usuario : listaUsuarios) {
            csvContent.append(String.format("%s, %s, %s, %s, %s, %s\n",
                    usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCnpj(),
                    usuario.getDataDeCriacao(), usuario.getTipoUsuario()));
        }

        byte[] csvBytes = csvContent.toString().getBytes();

        String fileName = "usuarios.csv";

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        MediaType mediaType = MediaType.parseMediaType("application/csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvBytes.length)
                .contentType(mediaType)
                .body(csvBytes);
    }



}
