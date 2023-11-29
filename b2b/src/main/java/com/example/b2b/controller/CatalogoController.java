package com.example.b2b.controller;

import com.example.b2b.dtos.catalogo.CatalogoResponseDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.services.CatalogoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/{idEmpresa}")
    public ResponseEntity<CatalogoResponseDTO> getCatalogoPorIdEmpresa(@PathVariable String idEmpresa) {
        Catalogo catalogo = catalogoService.getCatalogoPorIdEmpresa(idEmpresa);

        return ResponseEntity.status(200).body(new CatalogoResponseDTO(catalogo.getProdutos()));
    }

    @PutMapping("")
    public ResponseEntity<Catalogo> atualizarCatalogo(@PathVariable String idEmpresa,@Valid Catalogo catalogo) {
        Catalogo catalogoAtualizado = catalogoService.atualizarCatalogo(idEmpresa, catalogo);
        return ResponseEntity.status(200).body(catalogoAtualizado);
    }

}
