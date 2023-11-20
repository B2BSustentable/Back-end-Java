package com.example.b2b.services;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.entity.empresa.*;
import com.example.b2b.entity.empresa.roles.EmpresaBronze;
import com.example.b2b.entity.empresa.roles.EmpresaOuro;
import com.example.b2b.entity.empresa.roles.EmpresaPrata;
import com.example.b2b.repository.EmpresaRepository;
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
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public UserDetails findByEmail(String email) {
        return empresaRepository.findByEmail(email);
    }

    public List<Empresa> getTodosUsuarios() {
        return empresaRepository.findAll();
    }

    public Empresa cadastrarUsuario(String nome, String cnpj, String senhaEncriptada, LocalDateTime dataDeCricao, String email, TipoPlanos tipoPlanos, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {

        RegisterRequestDTO data = new RegisterRequestDTO(nome, cnpj, senhaEncriptada, dataDeCricao = LocalDateTime.now() , email, tipoPlanos, tipoAssinatura, limiteDeProdutos, desconto, suporte24h, acessoVIP);

        Empresa empresaExistente = (Empresa) empresaRepository.findByEmail(data.email());
        if (empresaExistente != null) {
            throw new IllegalStateException("Email já cadastrado");
        }

        Empresa novoEmpresa;
        switch (data.tipoPlanos()) {
            case USUARIO_BRONZE:
                novoEmpresa = new EmpresaBronze(data);
                break;
            case USUARIO_PRATA:
                novoEmpresa = new EmpresaPrata(data);
                break;
            case USUARIO_OURO:
                novoEmpresa = new EmpresaOuro(data);
                break;
            default:
                throw new IllegalStateException("O usuário inserido não é uma opção: " + data.tipoPlanos());
        }

        empresaRepository.save(novoEmpresa);

        return (novoEmpresa);
    }

    public Empresa getUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> usuario = empresaRepository.findByCnpj(cnpj);

        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            throw new IllegalStateException("Usuário não encontrado");
        }
    }

    public Empresa editarUsuarioPorCnpj(@RequestBody RegisterRequestDTO usuarioEditado, @PathVariable String cnpj) {
        // Verifique se o usuário com o mesmo CNPJ já existe
        Optional<Empresa> usuarioExistenteOptional = empresaRepository.findByCnpj(cnpj);

        if (usuarioExistenteOptional.isPresent()) {
            Empresa empresaExistente = usuarioExistenteOptional.get();

            // Atualize os campos do usuário existente com os valores do DTO editado
            empresaExistente.setNome(usuarioEditado.nome());
            empresaExistente.setCnpj(usuarioEditado.cnpj());
            empresaExistente.setSenha(usuarioEditado.senha());
            empresaExistente.setTipoPlanos(usuarioEditado.tipoPlanos());

            // Salve o usuário atualizado no banco de dados
            empresaRepository.save(empresaExistente);

            return (empresaExistente);
        } else {
            throw new IllegalStateException("Usuário não encontrado");
        }
    }

    public Void deletarUsuarioPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> verificarExistenciaDeUsuario = empresaRepository.findByCnpj(cnpj);
        if (verificarExistenciaDeUsuario.isPresent()) {
            empresaRepository.delete(verificarExistenciaDeUsuario.get());
        }

        throw new IllegalStateException("Usuário não encontrado");

    }

    public List<RegisterResponseDTO> convertListaResponseDTO(List<Empresa> listaEmpresas) {
        List<RegisterResponseDTO> listaUsuariosResponse = new ArrayList<>();
        for (Empresa empresa : listaEmpresas) {
            RegisterResponseDTO resposta = new RegisterResponseDTO(empresa.getNome(), empresa.getCnpj(), empresa.getDataDeCriacao(), empresa.getEmail(), empresa.getTipoPlanos().toString());
            listaUsuariosResponse.add(resposta);
        }
        return listaUsuariosResponse;
    }

    public List<Empresa> getListaOrdenadaPorData() {
           List<Empresa> listaDeEmpresas = empresaRepository.findAll();
           Lista<Empresa> lista = new Lista<>();
           lista.adicionarTodos(listaDeEmpresas);
           lista.selectionSortByDataDeCriacao();
           return lista.toList();
    }

    public Empresa getUsuarioPorData(LocalDateTime data) {
        List<Empresa> listaDeEmpresas = empresaRepository.findAll();
        Lista<Empresa> lista = new Lista<>();
        lista.adicionarTodos(listaDeEmpresas);
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




