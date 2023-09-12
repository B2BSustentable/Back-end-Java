package com.example.b2b.services;

import com.example.b2b.entity.Usuario;
import com.example.b2b.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuarioNovo) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(usuarioNovo.getCnpj()));
        if (verificarExistenciaDeUsuario.isPresent()) {
            return ResponseEntity.status(409).build();
        } else {
            return ResponseEntity.status(201).body(usuarioRepository.save(usuarioNovo));
        }
    }

    public ResponseEntity<Usuario> getUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));
        return verificarExistenciaDeUsuario.map(usuario -> ResponseEntity.status(200).body(usuario)).orElseGet(() -> ResponseEntity.status(404).build());
    }

    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody Usuario usuarioEditado, @PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));
        if (verificarExistenciaDeUsuario.isPresent()) {
            usuarioEditado.setId(verificarExistenciaDeUsuario.get().getId());
            return ResponseEntity.status(200).body(usuarioRepository.save(usuarioEditado));
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));
        if (verificarExistenciaDeUsuario.isPresent()) {
            usuarioRepository.delete(verificarExistenciaDeUsuario.get());
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    public ResponseEntity<Void> login(@RequestBody Usuario usuario) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(usuario.getCnpj()));
        if (verificarExistenciaDeUsuario.isPresent()) {
            if (usuario.getCnpj().equals(verificarExistenciaDeUsuario.get().getCnpj()) && usuario.getSenha().equals(verificarExistenciaDeUsuario.get().getSenha())) {
                return ResponseEntity.status(200).build();
            } else if (usuario.getNome().equals(verificarExistenciaDeUsuario.get().getNome()) && usuario.getSenha().equals(verificarExistenciaDeUsuario.get().getSenha())) {
                return ResponseEntity.status(200).build();
            }
        }
        return ResponseEntity.status(404).build();
    }

}
