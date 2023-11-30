package com.example.b2b.services;

import com.example.b2b.dtos.empresa.RegisterRequestDTO;
import com.example.b2b.dtos.empresa.RegisterResponseDTO;
import com.example.b2b.dtos.empresa.UpdateRequestDTO;
import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.dtos.produto.ProdutoResponseDTO;
import com.example.b2b.dtos.responsavel.ResponsavelRegisterResponseDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.empresa.*;
import com.example.b2b.entity.empresa.roles.EmpresaBasic;
import com.example.b2b.entity.empresa.roles.EmpresaPremium;
import com.example.b2b.entity.empresa.roles.EmpresaCommon;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.repository.EmpresaRepository;
import com.example.b2b.util.Lista;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

@Service
public class EmpresaService {

    @Value("${user.dir}")
    private String diretorioProjeto;
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ArquivoService imagemService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    CatalogoService catalogoService;

    @Autowired
    private PlanoService planoService;

    @Getter
    private Empresa empresaCadastrada;
    static List<ProdutoRequestDTO> listaLidaTxt = new ArrayList<ProdutoRequestDTO>();
    private Path caminhoArquivo;
    private Path caminhoCsv;
    private Path caminhoTxt;

    @PostConstruct
    private void initializePaths() {
        this.caminhoArquivo = Path.of(diretorioProjeto, "arquivo");
        this.caminhoCsv = Path.of(caminhoArquivo.toString(), "csv");
        this.caminhoTxt = Path.of(caminhoArquivo.toString(), "txt");
    }


    public UserDetails findByEmail(String email) {
        return empresaRepository.findByEmail(email);
    }

    public Empresa getEmpresaPorId(String idEmpresa) {
        Optional<Empresa> empresa = empresaRepository.findByuIdEmpresa(idEmpresa);

        if (empresa.isPresent()) {
            return empresa.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public List<Empresa> getTodasEmpresas() {
        Optional<List<Empresa>> listaEmpresas = Optional.ofNullable(empresaRepository.findAll());
        if (listaEmpresas.isPresent()) {
            return listaEmpresas.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Lista de empresas vazia");
        }
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de plano inválido");
        }
        empresaRepository.save(novaEmpresa);
        empresaCadastrada = novaEmpresa;

        Catalogo catalogoVazio = new Catalogo();
        catalogoVazio.setEmpresa(novaEmpresa);
        catalogoService.criarCatalogo(catalogoVazio);

        return (novaEmpresa);
    }

    public Empresa editarPlanoPorIdEmpresa(String idEmpresa, String tipoPlano) {
        Empresa empresa = getEmpresaPorId(idEmpresa);
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }

        switch (tipoPlano) {
            case "EMPRESA_BASIC":
                empresa.setTipoPlanos(TipoPlanos.EMPRESA_BASIC);
                planoService.configurarValoresBasicos((EmpresaBasic) empresa);
                break;
            case "EMPRESA_COMMON":
                empresa.setTipoPlanos(TipoPlanos.EMPRESA_COMMON);
                planoService.configurarValoresCommon((EmpresaCommon) empresa);
                break;
            case "EMPRESA_PREMIUM":
                empresa.setTipoPlanos(TipoPlanos.EMPRESA_PREMIUM);
                planoService.configurarValoresPremium((EmpresaPremium) empresa);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de plano inválido");
        }

        empresaRepository.save(empresa);

        return (empresa);
    }

    public Empresa getEmpresaPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaRepository.findByCnpj(cnpj);

        if (empresa.isPresent()) {
            return empresa.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public Empresa getEmpresaPorIdProduto(@PathVariable String idProduto) {
        Produto produto = produtoService.getProdutoPorUId(idProduto);

        if (produto != null) {
            return produto.getCatalogo().getEmpresa();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
    }

    public Empresa getEmpresaPorEmail(@PathVariable String email) {
        Optional<UserDetails> empresaExistente = Optional.ofNullable(empresaRepository.findByEmail(email));

        if (empresaExistente.isPresent()) {
            return (Empresa) empresaExistente.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public Empresa getEmpresaPorUIdEmpresa(@PathVariable String uIdEmpresa) {
        Optional<Empresa> empresaExistente = empresaRepository.findByuIdEmpresa(uIdEmpresa);

        if (empresaExistente.isPresent()) {
            return empresaExistente.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public String getLatitudePorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaRepository.findByCnpj(cnpj);

        if (empresa.isPresent()) {
            return empresa.get().getEndereco().get(0).getLatitude();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public String getLongitudePorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaRepository.findByCnpj(cnpj);

        if (empresa.isPresent()) {
            return empresa.get().getEndereco().get(0).getLongitude();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public Empresa editarEmpresaPorCnpj(MultipartFile file, UpdateRequestDTO empresaEditada, String cnpj) throws IOException {
        Optional<Empresa> empresaExistenteOptional = empresaRepository.findByCnpj(cnpj);

        if (empresaExistenteOptional.isPresent()) {
            Empresa empresaExistente = empresaExistenteOptional.get();

            String filePath = "";

            if (file != null) {
                filePath = imagemService.uploadImagem(file);
            }

            // Atualizar campos usando método auxiliar
            atualizarCampoSeNaoNulo(empresaEditada.nomeEmpresa(), empresaExistente::setNomeEmpresa);
            atualizarCampoSeNaoNulo(empresaEditada.email(), empresaExistente::setEmail);
            atualizarCampoSeNaoNulo(empresaEditada.senha(), empresaExistente::setSenha);
            atualizarCampoSeNaoNulo(empresaEditada.descricao(), empresaExistente::setDescricao);
            atualizarCampoSeNaoNulo(empresaEditada.enderecos(), empresaExistente::setEndereco);

            // Atualizar campos de foto
            atualizarCampoSeNaoNulo(filePath, empresaExistente::setPhoto);

            // Salve o usuário atualizado no banco de dados
            empresaRepository.save(empresaExistente);

            return empresaExistente;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

    public <T> void atualizarCampoSeNaoNulo(T novoValor, Consumer<T> atualizador) {
        if (novoValor != null) {
            atualizador.accept(novoValor);
        }
    }

    public Void deletarEmpresaPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> verificarExistenciaDeEmpresa = empresaRepository.findByCnpj(cnpj);
        if (verificarExistenciaDeEmpresa.isPresent()) {
            empresaRepository.delete(verificarExistenciaDeEmpresa.get());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");

    }

    public List<RegisterResponseDTO> convertListaResponseDTO(List<Empresa> listaEmpresas) {
        List<RegisterResponseDTO> listaEmpresaResponse = new ArrayList<>();
        for (Empresa empresa : listaEmpresas) {
            RegisterResponseDTO resposta = new RegisterResponseDTO(empresa.getNomeEmpresa(), empresa.getCnpj(), empresa.getDataDeCriacao(), empresa.getEmail(), empresa.getTipoPlanos(), empresa.getDescricao(), empresa.getPhoto(), empresa.getEndereco());
            listaEmpresaResponse.add(resposta);
        }
        return listaEmpresaResponse;
    }

    public Empresa getEmpresaPorData(LocalDateTime data) {
        List<Empresa> listaDeEmpresas = empresaRepository.findAll();
        Lista<Empresa> lista = new Lista<>();
        lista.adicionarTodos(listaDeEmpresas);
        lista.selectionSortByDataDeCriacao();
        return lista.buscaBinariaPorDataDeCriacao(data);
    }

    public String gerarEGravarArquivoCSV(List<ResponsavelRegisterResponseDTO> listaResponsaveisOrdenada, String nomeArq) {
        FileWriter arquivo = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";

        if (!this.caminhoArquivo.toFile().exists()) {
            this.caminhoArquivo.toFile().mkdir();
        }

        if (!this.caminhoCsv.toFile().exists()) {
            this.caminhoCsv.toFile().mkdir();
        }

        // Bloco try-catch para abrir o arquivo
        try {
            Path caminhoCsvCompleto = Path.of(this.caminhoCsv.toString(), nomeArq);
            File dest = new File(caminhoCsvCompleto.toString());
            arquivo = new FileWriter(nomeArq);
            saida = new Formatter(arquivo);
            arquivo = new FileWriter(dest);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        // Bloco try-catch para gravar o arquivo
        try {
            for (int i = 0; i < listaResponsaveisOrdenada.size(); i++) {
                ResponsavelRegisterResponseDTO responsavelRegisterResponseDTO = listaResponsaveisOrdenada.get(i);

                saida.format("%s;%s;%s;\n",
                        responsavelRegisterResponseDTO.nomeResponsavel(), responsavelRegisterResponseDTO.sobrenomeResponsavel(), responsavelRegisterResponseDTO.emailResponsavel());
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
            System.out.println("%s %s %s\n");

            while (entrada.hasNext()) {
                String nome = entrada.next();
                String sobrenome = entrada.next();
                String email = entrada.next();

                System.out.printf("%s - %s - %s\n", nome, sobrenome, email);
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

    public static void gravaRegistroTXT(String registro, String nomeArq) {
        BufferedWriter saida = null;

        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
        }

        try {
            saida.append(registro + "\n");
            saida.close();
        }
        catch (IOException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
        }
    }
    public static String gravarArquivoTXT(List<ProdutoResponseDTO> listaCatalogo, String nomeArq){
        int contaRegDadosGravados = 0;

        // Monta o registro de header
        String header = "00CATALOGO20232";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "01";

        // Grava o header
        gravaRegistroTXT(header, nomeArq);

        // Monta e grava os registros de dados (registros de corpo)
        for (ProdutoResponseDTO c : listaCatalogo) {
            String corpo = "02";
            corpo += String.format("%-32.32s", c.codigoDeBarras());
            corpo += String.format("%-15.15s", c.nomeProduto());
            corpo += String.format("%-25.25s", c.descricao());
            corpo += String.format("%-10.10s", c.categoria());

            gravaRegistroTXT(corpo, nomeArq);
            contaRegDadosGravados++;
        }

        String trailer = "01";
        trailer += String.format("%010d", contaRegDadosGravados);

        gravaRegistroTXT(trailer, nomeArq);
        return null;
    }

    public static void leArquivoTxt(String nomeArq) {
        BufferedReader entrada = null;
        String registro, tipoRegistro;
        String codigoDeBarras, nomeProduto, descricao, categoria;
        int contaRegDadosLidos = 0;
        int qtdRegDadosGravados;

        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
        }

        try {
            registro = entrada.readLine();

            while (registro != null) {

                tipoRegistro = registro.substring(0,2);
                if (tipoRegistro.equals("00")) {
                    System.out.println("Eh um registro de header");
                    System.out.println("Tipo de arquivo: " + registro.substring(2, 10));
                    System.out.println("Ano e semestre: " + registro.substring(10, 15));
                    System.out.println("Data e hora de gravação do arquivo: " + registro.substring(15, 34));
                    System.out.println("Versão do documento: " + registro.substring(34, 36));
                }
                else if (tipoRegistro.equals("01")) {
                    System.out.println("Eh um registro de trailer");
                    qtdRegDadosGravados = Integer.parseInt(registro.substring(2, 10));
                    if (qtdRegDadosGravados == contaRegDadosLidos) {
                        System.out.println("Quantidade de reg de dados gravados é compatível com a " +
                                "quantidade de reg de dados lidos");
                    }
                    else {
                        System.out.println("Quantidade de reg de dados gravados é incompatível com a " +
                                "quantidade de reg de dados lidos");
                    }
                }
                else if (tipoRegistro.equals("02")) {
                    System.out.println("Eh um registro de corpo");

                    codigoDeBarras = registro.substring(2, 34).trim();
                    nomeProduto = registro.substring(34, 49).trim();
                    descricao = registro.substring(49, 74).trim();
                    categoria = registro.substring(74, 84).trim();
                    contaRegDadosLidos++;

                    ProdutoRequestDTO c = new ProdutoRequestDTO(nomeProduto, categoria, descricao, codigoDeBarras);

                    listaLidaTxt.add(c);
                }
                else {
                    System.out.println("Eh um registro inválido");
                }

                registro = entrada.readLine();
            }

            entrada.close();

        }
        catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
            erro.printStackTrace();
        }


        System.out.println("\nLista lida:");
        for (ProdutoRequestDTO c : listaLidaTxt) {
            System.out.println(c);
        }

    }

    public List<Produto> importarTxtPorId(MultipartFile arquivo, String id, String nomeArq) {
        // Verifique se o usuário com o mesmo CNPJ já existe
        Optional<Empresa> empresaExistenteOptional = empresaRepository.findByuIdEmpresa(id);

        if (empresaExistenteOptional.isPresent()) {
            Empresa empresaExistente = empresaExistenteOptional.get();

            if(!this.caminhoArquivo.toFile().exists()) {
                this.caminhoArquivo.toFile().mkdirs();
            }

            if (!this.caminhoTxt.toFile().exists()) {
                if (!this.caminhoTxt.toFile().mkdirs()) {
                    throw new RuntimeException("Falha ao criar diretório: " + this.caminhoTxt);
                }
            }

            String filePath = this.caminhoTxt + "/" + nomeArq;
            File dest = new File(filePath);

            try {
                arquivo.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao salvar arquivo.", e);
            }

            leArquivoTxt(nomeArq);

            for (ProdutoRequestDTO c : listaLidaTxt) {

                produtoService.cadastrarProdutoPorIdEmpresa(c,id);
            }

        } else {
            throw new IllegalStateException("Empresa não encontrada");
        }

        return produtoService.getTodosProdutosPoruIdEmpresa(id);
    }


}




