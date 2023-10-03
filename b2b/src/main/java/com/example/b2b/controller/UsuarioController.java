package com.example.b2b.controller;

import com.example.b2b.dtos.usuario.RegisterDTO;
import com.example.b2b.entity.usuario.Usuario;
import com.example.b2b.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
