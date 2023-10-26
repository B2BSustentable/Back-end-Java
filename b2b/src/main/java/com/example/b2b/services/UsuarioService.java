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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static String gerarEGravarArquivoCSV(List<RegisterResponseDTO> listaUsuariosOrdenado, String nomeArq) {
        FileWriter arquivo = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";

        // Bloco try-catch para abrir o arquivo
        try {
            arquivo = new FileWriter(nomeArq);
            saida = new Formatter(arquivo);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        // Bloco try-catch para gravar o arquivo
        try {
            for (int i = 0; i < listaUsuariosOrdenado.size(); i++) {
                RegisterResponseDTO usuariosOrdenadoElemento = listaUsuariosOrdenado.get(i);

                saida.format("%s;%s;%s;%s;%s\n",
                        usuariosOrdenadoElemento.nome(), usuariosOrdenadoElemento.cnpj(), usuariosOrdenadoElemento.dataDeCriacao(), usuariosOrdenadoElemento.email(), usuariosOrdenadoElemento.tipoUsuario());
            }
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arquivo.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
        return ("O arquivo: " + nomeArq + " foi gerado com sucesso!");
    }

    public void leArquivoCsv(String nomeArq) {
        FileReader arq = null;
        Scanner entrada = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";

        // Bloco try-catch para abrir o arquivo
        try {
            arq = new FileReader(nomeArq);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        } catch (FileNotFoundException erro) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }

        // Bloco try-catch para ler o arquivo
        try {
            System.out.println("%s %s %s %s %s\n");

            while (entrada.hasNext()) {
                String nome = entrada.next();
                String cnpj = entrada.next();
                String dataDeCriacao = entrada.next();
                String email = entrada.next();
                String tipoUsuario = entrada.next();

                System.out.printf("%s - %s - %s - %s - %s\n", nome, cnpj, dataDeCriacao, email, tipoUsuario);
            }
        } catch (NoSuchElementException erro) {
            System.out.println("Arquivo com problemas");
            deuRuim = true;
        } catch (IllegalStateException erro) {
            System.out.println("Erro na leitura do arquivo");
            deuRuim = true;
        } finally {
            entrada.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }
}




