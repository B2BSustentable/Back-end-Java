package com.example.b2b.controller;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.dtos.produto.ProdutoResponseDTO;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.repository.ProdutoRepository;
import com.example.b2b.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // POST - http://localhost:8080/produto
    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO body){
        Produto resposta = produtoService.cadastrarProduto(body);
        return ResponseEntity.status(201).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getTodosProdutos(){
        List<Produto> listaProdutos = produtoService.getTodosProdutos();

        if (listaProdutos.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(listaProdutos);
        }
    }

    @GetMapping("/{uId}")
    public ResponseEntity<Produto> getProdutoPorUId(@PathVariable String uId){
        Produto produto = produtoService.getProdutoPorUId(uId);
        return ResponseEntity.status(200).body(produto);
    }

    @PutMapping("/{uId}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable String uId, @RequestBody @Valid ProdutoRequestDTO body){
        Produto resposta = produtoService.atualizarProduto(uId, body);
        return ResponseEntity.status(200).body(resposta);
    }

    @DeleteMapping("/{uId}")
    public ResponseEntity<Produto> deletarProduto(@PathVariable String uId){
        Void resposta = produtoService.deletarProduto(uId);
        return ResponseEntity.status(200).build();
    }
}
