package com.example.b2b.controller;

import com.example.b2b.entity.Usuario;
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
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuarioNovo) {
        List<Usuario> listaUsuarios = usuarioService.getTodosUsuarios();
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCnpj().equals(usuarioNovo.getCnpj())) {
                return ResponseEntity.status(409).build();
            }
        }
        Usuario usuarioCadastrado = usuarioService.cadastrarUsuario(usuarioNovo).getBody();
        return ResponseEntity.status(201).body(usuarioCadastrado);
    }

    // http://localhost:8080/usuarios/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody Usuario usuario, @PathVariable String cnpj) {
        List<Usuario> listaUsuarios = usuarioService.getTodosUsuarios();
        for (Usuario usuarioDaLista : listaUsuarios) {
            if (usuarioDaLista.getCnpj().equals(cnpj)) {
                usuario.setId(usuarioDaLista.getId());
                Usuario usuarioEditado = usuarioService.editarUsuarioPorCnpj(usuario, cnpj).getBody();
                return ResponseEntity.status(200).body(usuarioEditado);
            }
        }
        return ResponseEntity.status(404).build();
    }

    // http://localhost:8080/usuarios/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        List<Usuario> listaUsuarios = usuarioService.getTodosUsuarios();
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCnpj().equals(cnpj)) {
                usuarioService.deletarUsuarioPorCnpj(cnpj);
                return ResponseEntity.status(200).build();
            }
        }
        return ResponseEntity.status(404).build();
    }

    // http://localhost:8080/usuarios/login
    @GetMapping("/login")
        public ResponseEntity<Void> login(@RequestBody Usuario usuario) {
        ResponseEntity<Void> usuarioLogado = usuarioService.login(usuario);
        return usuarioLogado;
    }


}
