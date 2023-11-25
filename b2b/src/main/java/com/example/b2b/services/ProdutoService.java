package com.example.b2b.services;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.dtos.produto.ProdutoResponseDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CatalogoService catalogoService;

    public List<Produto> getTodosProdutos() {
        if (repository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum produto encontrado");
        } else {
            return repository.findAll();
        }
    }

    public Produto cadastrarProdutoPorIdEmpresa(ProdutoRequestDTO data, String idEmpresa) {
        Empresa empresa = empresaService.getEmpresaPorId(idEmpresa);
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }

        Produto novoProduto = new Produto(data);

        novoProduto.setNomeProduto(data.nomeProduto());
        novoProduto.setCategoria(data.categoria());
        novoProduto.setDescricao(data.descricao());
        novoProduto.setCodigoDeBarras(data.codigoDeBarras());

        catalogoService.adicionarProduto(novoProduto, idEmpresa);

        repository.save(novoProduto);

        return (novoProduto);
    }

    public Produto getProdutoPorUId(String id) {
        Optional<Produto> produto = repository.findById(id);
        if (produto.isPresent()) {
            return produto.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
    }

    public Produto atualizarProduto(String id, ProdutoRequestDTO data) {
        Optional<Produto> produto = repository.findById(id);
        if (produto.isPresent()) {
            produto.get().setNomeProduto(data.nomeProduto());
            produto.get().setCategoria(data.categoria());
            produto.get().setDescricao(data.descricao());
            produto.get().setCodigoDeBarras(data.codigoDeBarras());

            catalogoService.atualizarProduto(produto.get(), produto.get().getCatalogo().getEmpresa().getUIdEmpresa());

            repository.save(produto.get());

            return produto.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
    }

    public void deletarProduto(String id) {
        Optional<Produto> produto = repository.findById(id);
        if (produto.isPresent()) {
            catalogoService.removerProduto(produto.get(), produto.get().getCatalogo().getEmpresa().getUIdEmpresa());
            repository.delete(produto.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
    }

    public List<ProdutoResponseDTO> convertListaResponseDTO(List<Produto> listaProdutos){
        List<ProdutoResponseDTO> listaProdutosResponse = new ArrayList<>();
        for(Produto produto : listaProdutos){
            ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(produto.getNomeProduto(), produto.getCategoria(), produto.getDescricao(), produto.getCodigoDeBarras());
            listaProdutosResponse.add(produtoResponseDTO);
        }
        return listaProdutosResponse;
    }
}
