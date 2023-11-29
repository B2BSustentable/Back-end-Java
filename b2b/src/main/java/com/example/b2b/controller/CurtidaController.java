package com.example.b2b.controller;

import com.example.b2b.dtos.curtida.RequestCurtidaDTO;
import com.example.b2b.dtos.curtida.ResponseCurtidaDTO;
import com.example.b2b.entity.curtida.Curtida;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.services.CurtidaService;
import com.example.b2b.services.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curtida")
public class CurtidaController {
    @Autowired
    private CurtidaService curtidaService;

    @Autowired
    private ResponsavelService responsavelService;

    @PostMapping("/curtir")
    public void curtirProduto(@RequestParam RequestCurtidaDTO request, @PathVariable String uIdEmpresa) {
        curtidaService.curtirProduto(request.responsavel(), request.catalogo(), request.produto(), uIdEmpresa);
    }

    @PostMapping("/desfazer")
    public void desfazerCurtida(@RequestParam RequestCurtidaDTO request) {
        curtidaService.desfazerCurtida(request);
    }

    @PostMapping("/refazer")
    public void refazerCurtida(@RequestParam RequestCurtidaDTO request) {
        curtidaService.refazerCurtida(request);
    }

    @GetMapping("/listar/{responsavelId}")
    public List<ResponseCurtidaDTO> getCurtidasDoResponsavel(@PathVariable String responsavelId) {
        Responsavel responsavel = responsavelService.getResponsavelPorUId(responsavelId);
        List<Curtida> curtidas = curtidaService.getCurtidasDoResponsavel(responsavel);

        return curtidas.stream().map(curtida -> new ResponseCurtidaDTO(
                curtida.getResponsavelQueCurtiu(),
                curtida.getCatalogoDaEmpresa(),
                curtida.getProdutoCurtido(),
                curtida.isCurtido()
        )).toList();
    }

}
