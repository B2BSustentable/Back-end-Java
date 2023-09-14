package com.example.b2b.services;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.usuario.RegisterDTO;
import com.example.b2b.entity.usuario.*;
import com.example.b2b.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findByCnpj(String cnpj) {
        return usuarioRepository.findByCnpj(cnpj);
    }

    public UserDetails findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public ResponseEntity<Usuario> cadastrarUsuario(String nome, String cnpj, String senhaEncriptada, String email, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
        RegisterDTO data = new RegisterDTO(nome, cnpj, senhaEncriptada, email, tipoUsuario, tipoAssinatura, limiteDeProdutos, desconto, suporte24h, acessoVIP);
        Usuario usuarioExistente = (Usuario) usuarioRepository.findByEmail(data.email());
        if (usuarioExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

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

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    public ResponseEntity<Usuario> getUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(cnpj));
        return verificarExistenciaDeUsuario.map(usuario -> ResponseEntity.status(200).body(usuario)).orElseGet(() -> ResponseEntity.status(404).build());
    }

    public ResponseEntity<Usuario> editarUsuarioPorCnpj(@RequestBody RegisterDTO usuarioEditado, @PathVariable String cnpj) {
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

    public ResponseEntity<Void> login(@RequestBody AutenticacaoDTO usuario) {
        Optional<Usuario> verificarExistenciaDeUsuario = Optional.ofNullable(usuarioRepository.findByCnpj(usuario.email()));
        if (verificarExistenciaDeUsuario.isPresent()) {
            Usuario usuarioExistente = verificarExistenciaDeUsuario.get();
            if (usuarioExistente.getSenha().equals(usuario.senha())) {
                return ResponseEntity.status(200).build();
            } else {
                return ResponseEntity.status(401).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }

    }
}
