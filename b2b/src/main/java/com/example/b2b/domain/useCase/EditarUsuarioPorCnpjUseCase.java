package com.example.b2b.domain.useCase;

import com.example.b2b.domain.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public class EditarUsuarioPorCnpjUseCase {

    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody Usuario usuario, @PathVariable String cnpj, List<Usuario> listaUsuarios) {
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

}
