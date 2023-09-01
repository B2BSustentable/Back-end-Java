package com.example.b2b.domain.useCase;

import com.example.b2b.domain.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CadastrarUsuarioUseCase {

    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario, List<Usuario> listaUsuarios) {
        Optional<Usuario> verificarExistenciaDeUsuario = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(usuario.getCnpj())).findFirst();
        if (verificarExistenciaDeUsuario.isPresent()) {
            return ResponseEntity.status(409).build();
        } else {
            listaUsuarios.add(usuario);
            return ResponseEntity.status(201).body(usuario);
        }

    }
}
