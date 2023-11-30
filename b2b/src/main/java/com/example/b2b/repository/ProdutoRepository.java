package com.example.b2b.repository;

import com.example.b2b.dtos.produto.ProdutoResponseListaLatELongDTO;
import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Optional<Produto> findByIdProduto(String id);

    int countProdutoByCatalogoEmpresa(Empresa empresa);

    Optional<List<Produto>> findByNomeProdutoContainingIgnoreCase(String nomeProduto);

    Optional<List<Produto>> findByCategoriaContainingIgnoreCase(String categoria);

    Optional<Produto> findByCodigoDeBarras(String codigoDeBarras);

    @Query("SELECT p FROM produto p WHERE p.catalogo.empresa.uIdEmpresa = ?1")
    Optional<List<Produto>> findAllByCatalogoEmpresaUIdEmpresa(String uIdEmpresa);
}