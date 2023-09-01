package com.example.b2b.domain.useCase;

import com.example.b2b.domain.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public class EditarUsuarioPorCnpjUseCase {

    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody Usuario usuarioEditado, @PathVariable String cnpj, List<Usuario> listaUsuarios) {
        Usuario usuarioValidado = listaUsuarios.stream().filter(
                u -> u.getCnpj().equals(cnpj)).findFirst().orElse(null);

        if (!(usuarioValidado == null)) {
            listaUsuarios.set(listaUsuarios.indexOf(usuarioValidado), usuarioEditado);
            return ResponseEntity.status(200).body(usuarioEditado);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

}
