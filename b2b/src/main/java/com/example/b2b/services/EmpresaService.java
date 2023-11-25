package com.example.b2b.services;

import com.example.b2b.dtos.catalogo.CatalogoRequestDTO;
import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.dtos.empresa.UpdateRequestDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.empresa.*;
import com.example.b2b.entity.empresa.roles.EmpresaBasic;
import com.example.b2b.entity.empresa.roles.EmpresaPremium;
import com.example.b2b.entity.empresa.roles.EmpresaCommon;
import com.example.b2b.repository.EmpresaRepository;
import com.example.b2b.util.Lista;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    CatalogoService catalogoService;

    @Autowired
    private PlanoService planoService;

    @Getter
    private Empresa empresaCadastrada;

    private final Path caminhoImagem = Path.of(System.getProperty("user.dir") + "/arquivo"); // projeto

    public UserDetails findByEmail(String email) {
        return empresaRepository.findByEmail(email);
    }

    public Empresa getEmpresaPorId(String idEmpresa) {
        Optional<Empresa> empresa = empresaRepository.findByuIdEmpresa(idEmpresa);

        if (empresa.isPresent()) {
            return empresa.get();
        } else {
            throw new IllegalStateException("Empresa não encontrada");
        }
    }

    public List<Empresa> getTodasEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa cadastrarEmpresa(RegisterRequestDTO data) {
        Empresa empresaExistente = (Empresa) empresaRepository.findByEmail(data.email());
        if (empresaExistente != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"Email já cadastrado");
        }

        Optional<Empresa> existeByCnpj = empresaRepository.findByCnpj(data.cnpj());
        if (existeByCnpj.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"CNPJ já cadastrado");
        }

            Empresa novaEmpresa;
        switch (data.tipoPlanos()) {
            case EMPRESA_BASIC:
                novaEmpresa = new EmpresaBasic(data);
                planoService.configurarValoresBasicos((EmpresaBasic) novaEmpresa);
                novaEmpresa.setEndereco(new ArrayList<>());
                break;
            case EMPRESA_COMMON:
                novaEmpresa = new EmpresaCommon(data);
                planoService.configurarValoresCommon((EmpresaCommon) novaEmpresa);
                novaEmpresa.setEndereco(new ArrayList<>());
                break;
            case EMPRESA_PREMIUM:
                novaEmpresa = new EmpresaPremium(data);
                planoService.configurarValoresPremium((EmpresaPremium) novaEmpresa);
                novaEmpresa.setEndereco(new ArrayList<>());
                break;
            default:
                throw new IllegalStateException("A empresa inserida não é uma opção: " + data.tipoPlanos());
        }
        empresaRepository.save(novaEmpresa);
        empresaCadastrada = novaEmpresa;

        Catalogo catalogoVazio = new Catalogo();
        catalogoVazio.setEmpresa(novaEmpresa);
        catalogoService.criarCatalogo(catalogoVazio);

        return (novaEmpresa);
    }

    public Empresa getEmpresaPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaRepository.findByCnpj(cnpj);

        if (empresa.isPresent()) {
            return empresa.get();
        } else {
            throw new IllegalStateException("Empresa não encontrada");
        }
    }

    public Empresa editarEmpresaPorCnpj(MultipartFile foto, UpdateRequestDTO empresaEditada, String cnpj) {
        // Verifique se o usuário com o mesmo CNPJ já existe
        Optional<Empresa> empresaExistenteOptional = empresaRepository.findByCnpj(cnpj);

        if (empresaExistenteOptional.isPresent()) {
            Empresa empresaExistente = empresaExistenteOptional.get();

            if(!this.caminhoImagem.toFile().exists()) {
                this.caminhoImagem.toFile().mkdir();
            }

            String nomeArquivoFormatado = formatarNomeArquivo(foto.getOriginalFilename());
            String filePath = this.caminhoImagem + "/" + nomeArquivoFormatado;
            File dest = new File(filePath);

            try {
                foto.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao salvar a foto.", e);
            }


            // Atualize os campos do usuário existente com os valores do DTO editado
            empresaExistente.setNomeEmpresa(empresaEditada.nomeEmpresa());
            empresaExistente.setSenha(empresaEditada.senha());
            empresaExistente.setPhoto(filePath);


            // Salve o usuário atualizado no banco de dados
            empresaRepository.save(empresaExistente);

            return (empresaExistente);
        } else {
            throw new IllegalStateException("Empresa não encontrada");
        }
    }

    private String formatarNomeArquivo(String nomeOriginal) {
        return String.format("%s_%s", UUID.randomUUID(), nomeOriginal);
    }

    private String salvarFotoNoSistema(MultipartFile foto) {
        try {
            // Nomeie o arquivo de forma exclusiva, por exemplo, usando UUID
            String nomeArquivo = UUID.randomUUID().toString();

            // Caminho completo para o arquivo
            String caminhoCompleto = caminhoImagem + File.separator + nomeArquivo;

            // Salve a imagem no sistema de arquivos
            foto.transferTo(new File(caminhoCompleto));

            return caminhoCompleto;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar a foto.", e);
        }
    }



    public Void deletarEmpresaPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> verificarExistenciaDeEmpresa = empresaRepository.findByCnpj(cnpj);
        if (verificarExistenciaDeEmpresa.isPresent()) {
            empresaRepository.delete(verificarExistenciaDeEmpresa.get());
        }

        throw new IllegalStateException("Empresa não encontrado");

    }

    public List<RegisterResponseDTO> convertListaResponseDTO(List<Empresa> listaEmpresas) {
        List<RegisterResponseDTO> listaEmpresaResponse = new ArrayList<>();
        for (Empresa empresa : listaEmpresas) {
            RegisterResponseDTO resposta = new RegisterResponseDTO(empresa.getNomeEmpresa(), empresa.getCnpj(), empresa.getDataDeCriacao(), empresa.getEmail(), empresa.getTipoPlanos(), empresa.getDescricao(), empresa.getPhoto(), empresa.getEndereco());
            listaEmpresaResponse.add(resposta);
        }
        return listaEmpresaResponse;
    }

    public List<Empresa> getListaOrdenadaPorData() {
           List<Empresa> listaDeEmpresas = empresaRepository.findAll();
           Lista<Empresa> lista = new Lista<>();
           lista.adicionarTodos(listaDeEmpresas);
           lista.selectionSortByDataDeCriacao();
           return lista.toList();
    }

    public Empresa getEmpresaPorData(LocalDateTime data) {
        List<Empresa> listaDeEmpresas = empresaRepository.findAll();
        Lista<Empresa> lista = new Lista<>();
        lista.adicionarTodos(listaDeEmpresas);
        lista.selectionSortByDataDeCriacao();
        return lista.buscaBinariaPorDataDeCriacao(data);
    }

    public static String gerarEGravarArquivoCSV(List<RegisterResponseDTO> listaEmpresaOrdenada, String nomeArq) {
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
            for (int i = 0; i < listaEmpresaOrdenada.size(); i++) {
                RegisterResponseDTO EmpresasOrdenadoElemento = listaEmpresaOrdenada.get(i);

                saida.format("%s;%s;%s;%s;%s\n",
                        EmpresasOrdenadoElemento.nomeEmpresa(), EmpresasOrdenadoElemento.cnpj(), EmpresasOrdenadoElemento.dataDeCriacao(), EmpresasOrdenadoElemento.email(), EmpresasOrdenadoElemento.tipoPlanos());
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
                String tipoEmpresa = entrada.next();

                System.out.printf("%s - %s - %s - %s - %s\n", nome, cnpj, dataDeCriacao, email, tipoEmpresa);
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




