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
@RequestMapping("produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // POST - http://localhost:8080/produto
    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO body){
        ResponseEntity<Produto> resposta = produtoService.cadastrarProduto(body);

        if (resposta.getStatusCode().is2xxSuccessful()) {
            return resposta;
        } else {
            return ResponseEntity.status(resposta.getStatusCode()).build();
        }
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
}
