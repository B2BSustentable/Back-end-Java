package com.example.b2b.repository;

import com.example.b2b.entity.catalogo.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {

    @Query("SELECT c FROM catalogo c WHERE c.empresa.uIdEmpresa = :uIdEmpresa")
    Optional<Catalogo> findByEmpresaUId(@Param("uIdEmpresa") String uIdEmpresa);

    Optional<Catalogo> findByIdCatalogo(String id);
}
