package com.example.b2b.controller;

import com.example.b2b.domain.entity.Usuario;
import com.example.b2b.domain.useCase.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    public List<Usuario> listaUsuarios = new ArrayList<>();

    // http://localhost:8080/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        if (listaUsuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(listaUsuarios);
        }
    }

    // http://localhost:8080/usuarios/123456789
    @GetMapping("/{cnpj}")
    public ResponseEntity<Usuario> getUsuarioPorCnpj(@PathVariable String cnpj) {
        GetUsuarioPorCnpjUseCase getUsuarioPorCnpjUseCase = new GetUsuarioPorCnpjUseCase();
        return getUsuarioPorCnpjUseCase.getUsarioPorCnpj(cnpj, listaUsuarios);
    }

    // http://localhost:8080/usuarios
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        CadastrarUsuarioUseCase cadastrarUsuarioUseCase = new CadastrarUsuarioUseCase();
        return cadastrarUsuarioUseCase.cadastrarUsuario(usuario, listaUsuarios);
    }

    // http://localhost:8080/usuarios/123456789
    @PutMapping("/{cnpj}")
    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody Usuario usuario, @PathVariable String cnpj) {
        EditarUsuarioPorCnpjUseCase editarUsuarioPorCnpjUseCase = new EditarUsuarioPorCnpjUseCase();
        return editarUsuarioPorCnpjUseCase.editarUsuarioPorCnpj(usuario, cnpj, listaUsuarios);
    }

    // http://localhost:8080/usuarios/123456789
    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        DeletarUsuarioPorCnpjUseCase deletarUsuarioPorCnpj = new DeletarUsuarioPorCnpjUseCase();
        return deletarUsuarioPorCnpj.deletarUsuarioPorCnpj(cnpj, listaUsuarios);
    }

    // http://localhost:8080/usuarios/login
    @GetMapping("/login")
        public ResponseEntity<Void> login(@RequestBody Usuario usuario) {
        GetLoginUseCase getLoginUseCase = new GetLoginUseCase();
        return getLoginUseCase.getLogin(usuario);
    }


}
