package com.example.b2b.controller;

import com.example.b2b.dtos.produto.ProdutoRequestDTO;
import com.example.b2b.dtos.produto.ProdutoResponseDTO;
import com.example.b2b.dtos.produto.ProdutoResponseListaLatELongDTO;
import com.example.b2b.entity.produto.Produto;
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
    @PostMapping("/{idEmpresa}")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO body, @PathVariable String idEmpresa){
        Produto produtoEntity = produtoService.cadastrarProdutoPorIdEmpresa(body, idEmpresa);
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(produtoEntity.getNomeProduto(), produtoEntity.getCategoria(), produtoEntity.getDescricao(), produtoEntity.getCodigoDeBarras());
        return ResponseEntity.status(201).body(produtoResponseDTO);
    }

    @GetMapping("/nomeParcial/{uIdEmpresa}")
    public ResponseEntity<List<ProdutoResponseListaLatELongDTO>> getProdutoPorNomeParcialIgnoandoCase(@RequestParam String nomeProduto, @PathVariable String uIdEmpresa){
        List<Produto> listaProdutos = produtoService.getProdutosPorNomeParcial(nomeProduto, uIdEmpresa);
        List<ProdutoResponseListaLatELongDTO> listaProdutosResponse = produtoService.convertListaLatELongResponseDTO(listaProdutos, uIdEmpresa);
        return ResponseEntity.status(200).body(listaProdutosResponse);
    }

    @GetMapping("/categoriaParcial/{uIdEmpresa}")
    public ResponseEntity<List<ProdutoResponseDTO>> getProdutoPorCategoriaParcialIgnoandoCase(@RequestParam String categoria, @PathVariable String uIdEmpresa){
        List<Produto> listaProdutos = produtoService.getProdutosPorCategoriaParcial(categoria, uIdEmpresa);
        List<ProdutoResponseDTO> listaProdutosResponse = produtoService.convertListaResponseDTO(listaProdutos);
        return ResponseEntity.status(200).body(listaProdutosResponse);
    }

    @GetMapping("/{uIdEmpresa}/empresa")
    public ResponseEntity<List<ProdutoResponseDTO>> getTodosProdutos(@PathVariable String uIdEmpresa){
        List<Produto> listaProdutos = produtoService.getTodosProdutosPoruIdEmpresa(uIdEmpresa);
        List<ProdutoResponseDTO> listaProdutosResponse = produtoService.convertListaResponseDTO(listaProdutos);
        return ResponseEntity.status(200).body(listaProdutosResponse);
    }

    @GetMapping("/{uId}")
    public ResponseEntity<ProdutoResponseDTO> getProdutoPorUId(@PathVariable String uId){
        Produto produtoEntity = produtoService.getProdutoPorUId(uId);
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(produtoEntity.getNomeProduto(), produtoEntity.getCategoria(), produtoEntity.getDescricao(), produtoEntity.getCodigoDeBarras());
        return ResponseEntity.status(200).body(produtoResponseDTO);
    }

    @PutMapping("/{uId}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable String uId, @RequestBody @Valid ProdutoRequestDTO body){
        Produto produtoEntity = produtoService.atualizarProduto(body, uId);
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(produtoEntity.getNomeProduto(), produtoEntity.getCategoria(), produtoEntity.getDescricao(), produtoEntity.getCodigoDeBarras());
        return ResponseEntity.status(200).body(produtoResponseDTO);
    }

    @DeleteMapping("/{uId}")
    public ResponseEntity deletarProduto(@PathVariable String uId){
        produtoService.deletarProduto(uId);
        return ResponseEntity.status(200).build();
    }
}
