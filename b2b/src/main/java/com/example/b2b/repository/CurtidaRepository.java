package com.example.b2b.repository;

import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.curtida.Curtida;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.entity.responsavel.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurtidaRepository extends JpaRepository<Curtida, String> {
    List<Curtida> findByResponsavelQueCurtiuAndCatalogoDaEmpresaAndProdutoCurtido(
            Responsavel responsavel, Catalogo catalogo, Produto produto);

    List<Curtida> findByResponsavelQueCurtiu(Responsavel responsavel);
}
