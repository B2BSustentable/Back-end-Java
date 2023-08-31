package com.example.b2b.controller;

import com.example.b2b.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private List<Usuario> listaUsuarios = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return ResponseEntity.status(200).body(listaUsuarios);
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<Usuario> getUsarioPorCnpj(@PathVariable String cnpj) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(cnpj)).findFirst().orElse(null);


        if (!(usuarioValidado == null)) {
            return ResponseEntity.status(200).body(usuarioValidado);
        } else {
            return ResponseEntity.status(404).build();
        }

    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Optional<Usuario> verificarExistenciaDeUsuario = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(usuario.getCnpj())).findFirst();
        if (verificarExistenciaDeUsuario.isPresent()) {
            return ResponseEntity.status(409).build();
        } else {
            listaUsuarios.add(usuario);
            return ResponseEntity.status(201).body(usuario);
        }

    }

    @PutMapping("/{cnpj}")
    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody Usuario usuario, @PathVariable String cnpj) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(cnpj)).findFirst().orElse(null);

        if (!(usuarioValidado == null)) {
            listaUsuarios.remove(usuarioValidado);
            listaUsuarios.add(usuario);
            return ResponseEntity.status(200).body(usuario);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(cnpj)).findFirst().orElse(null);

        if (!(usuarioValidado == null)) {
            listaUsuarios.remove(usuarioValidado);
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Void> getLogin(@RequestBody Usuario usuario) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.equals(usuario)).findFirst().orElse(null);
        if (usuarioValidado == null) {
            return ResponseEntity.status(404).build();
        } else if (usuario.getCnpj().equals(usuarioValidado.getCnpj()) && usuario.getSenha().equals(usuarioValidado.getSenha())) {
            return ResponseEntity.status(200).build();
        } else if (usuario.getNome().equals(usuarioValidado.getNome()) && usuario.getSenha().equals(usuarioValidado.getSenha())) {
            return ResponseEntity.status(200).build();
        }

        return ResponseEntity.status(404).build();
    }


}
