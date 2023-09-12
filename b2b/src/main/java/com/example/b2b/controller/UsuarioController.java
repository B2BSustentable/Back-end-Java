package com.example.b2b.controller;

import com.example.b2b.dtos.UsuarioDTO;
import com.example.b2b.entity.usuario.Usuario;
import com.example.b2b.services.UsuarioService;
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
    @PostMapping("/{cnpj}/postagem")
    public ResponseEntity<?> fazerPostagem(@PathVariable String cnpj, @RequestBody String conteudo) {
        Usuario usuario = usuarioService.getUsuarioPorCnpj(cnpj).getBody();
        if (usuario != null) {
            return ResponseEntity.status(200).body(usuario.fazerPostagem(conteudo));
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // http://localhost:8080/usuarios/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<Usuario> getUsuarioPorCnpj(@PathVariable String cnpj) {
        ResponseEntity<Usuario> usuarioCnpj = usuarioService.getUsuarioPorCnpj(cnpj);
        if (usuarioCnpj.getBody().getCnpj().equals(cnpj)) {
            return ResponseEntity.status(200).body(usuarioCnpj.getBody());
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // http://localhost:8080/usuarios
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody UsuarioDTO usuarioNovo) {
        ResponseEntity<Usuario> resposta = usuarioService.cadastrarUsuario(usuarioNovo);
        if (resposta.getStatusCode().is2xxSuccessful()) {
            return resposta;
        } else {
            return ResponseEntity.status(resposta.getStatusCode()).build();
        }
    }

    // http://localhost:8080/usuarios/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody UsuarioDTO usuario, @PathVariable String cnpj) {
        ResponseEntity<Usuario> resposta = usuarioService.editarUsuarioPorCnpj(usuario, cnpj);
        if (resposta.getStatusCode().is2xxSuccessful()) {
            return resposta;
        } else {
            return ResponseEntity.status(resposta.getStatusCode()).build();
        }
    }

    // http://localhost:8080/usuarios/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        ResponseEntity<Usuario> resposta = usuarioService.deletarUsuarioPorCnpj(cnpj);
        if (resposta.getStatusCode().is2xxSuccessful()) {
            return resposta;
        } else {
            return ResponseEntity.status(resposta.getStatusCode()).build();
        }
    }

    // http://localhost:8080/usuarios/login
    @GetMapping("/login")
        public ResponseEntity<Void> login(@RequestBody Usuario usuario) {
        ResponseEntity<Void> usuarioLogado = usuarioService.login(usuario);
        return usuarioLogado;
    }


}
