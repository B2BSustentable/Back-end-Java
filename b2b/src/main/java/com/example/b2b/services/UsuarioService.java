package com.example.b2b.services;

import com.example.b2b.dtos.autenticacao.AutenticacaoDTO;
import com.example.b2b.dtos.usuario.RegisterDTO;
import com.example.b2b.entity.usuario.*;
import com.example.b2b.repository.UsuarioRepository;
import com.example.b2b.util.Lista;
import com.example.b2b.util.UsuarioSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UserDetails findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario cadastrarUsuario(String nome, String cnpj, String senhaEncriptada, LocalDate dataC, String email, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {

        RegisterDTO data = new RegisterDTO(nome, cnpj, senhaEncriptada, LocalDate.now() , email, tipoUsuario, tipoAssinatura, limiteDeProdutos, desconto, suporte24h, acessoVIP);

        Usuario usuarioExistente = (Usuario) usuarioRepository.findByEmail(data.email());
        if (usuarioExistente != null) {
            throw new IllegalStateException("Email já cadastrado");
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
                throw new IllegalStateException("O usuário inserido não é uma opção: " + data.tipoUsuario());
        }

        usuarioRepository.save(novoUsuario);

        return (novoUsuario);
    }

    public Usuario getUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> usuario = usuarioRepository.findByCnpj(cnpj);

        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new IllegalStateException("Usuário não encontrado");
        }
    }

    public Usuario editarUsuarioPorCnpj(@RequestBody RegisterDTO usuarioEditado, @PathVariable String cnpj) {
        // Verifique se o usuário com o mesmo CNPJ já existe
        Optional<Usuario> usuarioExistenteOptional = usuarioRepository.findByCnpj(cnpj);

        if (usuarioExistenteOptional.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOptional.get();

            // Atualize os campos do usuário existente com os valores do DTO editado
            usuarioExistente.setNome(usuarioEditado.nome());
            usuarioExistente.setCnpj(usuarioEditado.cnpj());
            usuarioExistente.setSenha(usuarioEditado.senha());
            usuarioExistente.setTipoUsuario(usuarioEditado.tipoUsuario());

            // Salve o usuário atualizado no banco de dados
            usuarioRepository.save(usuarioExistente);

            return (usuarioExistente);
        } else {
            throw new IllegalStateException("Usuário não encontrado");
        }
    }

    public Void deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Usuario> verificarExistenciaDeUsuario = usuarioRepository.findByCnpj(cnpj);
        if (verificarExistenciaDeUsuario.isPresent()) {
            usuarioRepository.delete(verificarExistenciaDeUsuario.get());
        }

        throw new IllegalStateException("Usuário não encontrado");

    }

    public List<Usuario> getListaOrdenadaPorData() {
           List<Usuario> listaDeUsuarios = usuarioRepository.findAll();
           Lista<Usuario> lista = new Lista<>();
           lista.adicionarTodos(listaDeUsuarios);
           lista.selectionSortByDataDeCriacao();
           return lista.toList();
    }

    public Usuario getUsuarioPorData(LocalDate data) {
        List<Usuario> listaDeUsuarios = usuarioRepository.findAll();
        Lista<Usuario> lista = new Lista<>();
        lista.adicionarTodos(listaDeUsuarios);
        lista.selectionSortByDataDeCriacao();
        return lista.buscaBinariaPorDataDeCriacao(data);
    }

}


