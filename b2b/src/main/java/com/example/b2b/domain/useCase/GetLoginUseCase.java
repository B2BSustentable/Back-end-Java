package com.example.b2b.domain.useCase;

import com.example.b2b.domain.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public class GetLoginUseCase {

    private List<Usuario> listaUsuarios = new ArrayList<>();

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
