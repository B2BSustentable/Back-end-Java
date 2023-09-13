package com.example.b2b.services;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> getTodosProdutos() {
        return repository.findAll();
    }

    public ResponseEntity<Produto> cadastrarProduto(ProdutoRequestDTO data) {
        Produto produtoExistente = repository.findByNome(data.nome());
        if (produtoExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Produto novoProduto = new Produto(data);

        repository.save(novoProduto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    public ResponseEntity<Produto> getProdutoPorUId(String id) {
        Produto produto = repository.findById(id);
        if (produto != null) {
            return ResponseEntity.status(HttpStatus.OK).body(produto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Produto> atualizarProduto(String id, ProdutoRequestDTO data) {
        Produto produto = repository.findById(id);
        if (produto != null) {
            produto.setNome(data.nome());
            produto.setPreco(data.preco());
            repository.save(produto);
            return ResponseEntity.status(HttpStatus.OK).body(produto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Produto> deletarProduto(String id) {
        Produto produto = repository.findById(id);
        if (produto != null) {
            repository.delete(produto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
