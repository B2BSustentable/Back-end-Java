package com.example.b2b.domain.useCase;

import com.example.b2b.domain.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

public class DeletarUsuarioPorCnpjUseCase {

    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj, List<Usuario> listaUsuarios) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(cnpj)).findFirst().orElse(null);

        if (!(usuarioValidado == null)) {
            listaUsuarios.remove(usuarioValidado);
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

}
