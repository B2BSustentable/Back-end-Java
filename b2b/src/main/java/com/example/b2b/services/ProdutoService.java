package com.example.b2b.services;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> getTodosProdutos() {
        return repository.findAll();
    }

    public Produto cadastrarProduto(ProdutoRequestDTO data) {
        Optional<Produto> produtoExistente = repository.findByNome(data.nome());
        if (produtoExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto já cadastrado");
        }

        Produto novoProduto = new Produto(data);

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
            produto.get().setNome(data.nome());
            produto.get().setPreco(data.preco());
            repository.save(produto.get());

            return produto.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
    }

    public Void deletarProduto(String id) {
        Optional<Produto> produto = repository.findById(id);
        if (produto.isPresent()) {
            repository.delete(produto.get());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");

    }
}
