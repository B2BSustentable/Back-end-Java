package com.example.b2b.repository;

import com.example.b2b.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Optional<Produto> findById(String id);

    Optional<List<Produto>> findByNomeProdutoContainingIgnoreCase(String nomeProduto);
}