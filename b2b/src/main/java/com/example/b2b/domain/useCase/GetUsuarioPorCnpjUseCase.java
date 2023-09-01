package com.example.b2b.domain.useCase;

import com.example.b2b.domain.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

public class GetUsuarioPorCnpjUseCase {

    public ResponseEntity<Usuario> getUsarioPorCnpj(@PathVariable String cnpj, List<Usuario> listaUsuarios) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(cnpj)).findFirst().orElse(null);


        if (!(usuarioValidado == null)) {
            return ResponseEntity.status(200).body(usuarioValidado);
        } else {
            return ResponseEntity.status(404).build();
        }

    }
}
