package com.example.b2b.services;

import com.example.b2b.dtos.curtida.RequestCurtidaDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.curtida.Curtida;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.entity.responsavel.Responsavel;
import com.example.b2b.repository.CurtidaRepository;
import com.example.b2b.util.PilhaObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CurtidaService {
    @Autowired
    private CurtidaRepository curtidaRepository;
    @Autowired
    private EmpresaService empresaService;
    private PilhaObj<Curtida> undoStack = new PilhaObj<>(3);
    private PilhaObj<Curtida> redoStack = new PilhaObj<>(3);

    public void curtirProduto(Responsavel responsavel, Catalogo catalogo, Produto produto, String uIdEmpresa) {

        if (empresaService.getEmpresaPorId(uIdEmpresa).getPlano().isAddFavoritos() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Plano não permite curtidas");
        }

        Curtida curtida = new Curtida();
        curtida.setResponsavelQueCurtiu(responsavel);
        curtida.setCatalogoDaEmpresa(catalogo);
        curtida.setProdutoCurtido(produto);
        curtida.setCurtido(true);

        curtidaRepository.save(curtida);
        undoStack.push(curtida);
        redoStack.clear();
    }

    public void desfazerCurtida(RequestCurtidaDTO request) {
        if (!undoStack.isEmpty()) {
            Curtida curtidaDesfeita = undoStack.pop();
            curtidaDesfeita.setCurtido(false);
            curtidaRepository.save(curtidaDesfeita);
            redoStack.push(curtidaDesfeita);
        }
    }

    public void refazerCurtida(RequestCurtidaDTO request) {
        if (!redoStack.isEmpty()) {
            Curtida curtidaRefazida = redoStack.pop();
            curtidaRefazida.setCurtido(true);
            curtidaRepository.save(curtidaRefazida);
            undoStack.push(curtidaRefazida);
        }
    }

    public List<Curtida> getCurtidasDoResponsavel(Responsavel responsavel) {
        return curtidaRepository.findByResponsavelQueCurtiu(responsavel);
    }
}
