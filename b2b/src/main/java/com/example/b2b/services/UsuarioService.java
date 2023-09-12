package com.example.b2b.services;

import com.example.b2b.dtos.UsuarioDTO;
import com.example.b2b.entity.Usuario;
import com.example.b2b.entity.UsuarioBronze;
import com.example.b2b.entity.UsuarioOuro;
import com.example.b2b.entity.UsuarioPrata;
import com.example.b2b.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import static com.example.b2b.entity.TipoUsuario.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public ResponseEntity<Usuario> cadastrarUsuario(UsuarioDTO data) {
        // Verifique se o usuário com o mesmo CNPJ já existe
        Usuario usuarioExistente = usuarioRepository.findByCnpj(data.cnpj());
        if (usuarioExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Crie um novo usuário com base no tipo fornecido no UsuarioDTO
        Usuario novoUsuario;
        switch (data.tipoUsuario()) {
            case USUARIO_BRONZE:
                novoUsuario = new UsuarioBronze(data);
                break;
            case USUARIO_PRATA:
                novoUsuario = new UsuarioPrata(data);
                break;
            case USUARIO_OURO:
                novoUsuario = new UsuarioOuro(data);
                break;
            default:
                // Tipo de usuário inválido
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Salve o novo usuário no banco de dados
        usuarioRepository.save(novoUsuario);

        // Retorne a resposta com o novo usuário e o status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    public ResponseEntity<Usuario> getUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));
        return verificarExistenciaDeUsuario.map(usuario -> ResponseEntity.status(200).body(usuario)).orElseGet(() -> ResponseEntity.status(404).build());
    }

    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody UsuarioDTO usuarioEditado, @PathVariable String cnpj) {
        // Verifique se o usuário com o mesmo CNPJ já existe
        Optional<Usuario> usuarioExistenteOptional = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));

        if (usuarioExistenteOptional.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOptional.get();

            // Atualize os campos do usuário existente com os valores do DTO editado
            usuarioExistente.setNome(usuarioEditado.nome());
            usuarioExistente.setCnpj(usuarioEditado.cnpj());
            usuarioExistente.setSenha(usuarioEditado.senha());
            usuarioExistente.setTipoUsuario(usuarioEditado.tipoUsuario());

            // Salve o usuário atualizado no banco de dados
            usuarioRepository.save(usuarioExistente);

            return ResponseEntity.status(HttpStatus.OK).body(usuarioExistente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Usuario> deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));
        if (verificarExistenciaDeUsuario.isPresent()) {
            usuarioRepository.delete(verificarExistenciaDeUsuario.get());
            return ResponseEntity.status(204).build();
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
