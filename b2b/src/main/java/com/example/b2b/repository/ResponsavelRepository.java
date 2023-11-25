package com.example.b2b.repository;

import com.example.b2b.entity.responsavel.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {
    Optional<Responsavel> findByEmailResponsavel(String email);
    Optional<Responsavel> findByUIdResponsavel(String uIdResponsavel);
}
