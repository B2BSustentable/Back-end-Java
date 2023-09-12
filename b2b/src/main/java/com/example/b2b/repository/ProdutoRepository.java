package com.example.b2b.repository;

import com.example.b2b.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Produto findByNome(String nome);
}