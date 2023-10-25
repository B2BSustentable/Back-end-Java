package com.example.b2b.services;

import com.example.b2b.dtos.usuario.RegisterRequestDTO;
import com.example.b2b.dtos.usuario.RegisterResponseDTO;
import com.example.b2b.entity.usuario.*;
import com.example.b2b.repository.UsuarioRepository;
import com.example.b2b.util.Lista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Usuario cadastrarUsuario(String nome, String cnpj, String senhaEncriptada, LocalDateTime dataDeCricao, String email, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {

        RegisterRequestDTO data = new RegisterRequestDTO(nome, cnpj, senhaEncriptada, dataDeCricao = LocalDateTime.now() , email, tipoUsuario, tipoAssinatura, limiteDeProdutos, desconto, suporte24h, acessoVIP);

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

    public Usuario editarUsuarioPorCnpj(@RequestBody RegisterRequestDTO usuarioEditado, @PathVariable String cnpj) {
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

    public List<RegisterResponseDTO> convertListaResponseDTO(List<Usuario> listaUsuarios) {
        List<RegisterResponseDTO> listaUsuariosResponse = new ArrayList<>();
        for (Usuario usuario : listaUsuarios) {
            RegisterResponseDTO resposta = new RegisterResponseDTO(usuario.getNome(), usuario.getCnpj(), usuario.getDataDeCriacao(), usuario.getEmail(), usuario.getTipoUsuario().toString());
            listaUsuariosResponse.add(resposta);
        }
        return listaUsuariosResponse;
    }

    public List<Usuario> getListaOrdenadaPorData() {
           List<Usuario> listaDeUsuarios = usuarioRepository.findAll();
           Lista<Usuario> lista = new Lista<>();
           lista.adicionarTodos(listaDeUsuarios);
           lista.selectionSortByDataDeCriacao();
           return lista.toList();
    }

    public Usuario getUsuarioPorData(LocalDateTime data) {
        List<Usuario> listaDeUsuarios = usuarioRepository.findAll();
        Lista<Usuario> lista = new Lista<>();
        lista.adicionarTodos(listaDeUsuarios);
        lista.selectionSortByDataDeCriacao();
        return lista.buscaBinariaPorDataDeCriacao(data);
    }

    public String gerarEGravarArquivoCSV() {
        String nomeArquivo = "usuarios_ordenados.csv";
        String caminhoDoArquivo = "com/example/b2b/arquivo/" + nomeArquivo;

        try {
            Path directory = Paths.get(caminhoDoArquivo).getParent();

            Files.createDirectories(directory);

            StringBuilder csvContent = new StringBuilder();
            csvContent.append("ID, Nome, Email, CNPJ, Data de Criação, Tipo de Usuário\n");

            for (Usuario usuario : getListaOrdenadaPorData()) {
                csvContent.append(String.format("%s, %s, %s, %s, %s, %s\n",
                        usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCnpj(),
                        usuario.getDataDeCriacao().toString(), usuario.getTipoUsuario()));
            }

            // Grava o conteúdo no arquivo
            Files.write(Paths.get(caminhoDoArquivo), csvContent.toString().getBytes(), StandardOpenOption.CREATE);
            System.out.println("Arquivo CSV gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
            caminhoDoArquivo = "Erro ao gerar o arquivo CSV.";
        }

        return caminhoDoArquivo;
    }



        public String gerarEGravarArquivoCSVB(List<Usuario> listaUsuariosOrdenado) {
            String nomeArquivo = "usuarios_ordenados.csv";
            String caminhoDoArquivo = "com/example/b2b/arquivo/" + nomeArquivo;

            try (FileWriter fileWriter = new FileWriter(caminhoDoArquivo)) {


                for (Usuario usuario : listaUsuariosOrdenado) {
                    fileWriter.append(String.format("%s, %s, %s, %s, %s, %s\n",
                            usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCnpj(),
                            usuario.getDataDeCriacao(), usuario.getTipoUsuario()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                caminhoDoArquivo = "Erro ao gerar o arquivo CSV.";
            }

            return caminhoDoArquivo;
        }
    }




