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
}
