package com.example.b2b.services;

import com.example.b2b.dtos.catalogo.CatalogoRequestDTO;
import com.example.b2b.entity.catalogo.Catalogo;
import com.example.b2b.entity.produto.Produto;
import com.example.b2b.repository.CatalogoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;

    public CatalogoService(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

   public void criarCatalogo(Catalogo catalogo) {
       Optional<Catalogo> catalogoExistente = catalogoRepository.findByEmpresaUId(catalogo.getEmpresa().getUIdEmpresa());

       if (catalogoExistente.isPresent()) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "Catalogo já cadastrado");
         }

        Catalogo novoCatalogo = new Catalogo();
        novoCatalogo.setEmpresa(catalogo.getEmpresa());

        catalogoRepository.save(novoCatalogo);
   }

    public Catalogo atualizarCatalogo(String id, Catalogo catalogo) {
         Optional<Catalogo> catalogoExistente = catalogoRepository.findByIdCatalogo(id);

         if (catalogoExistente.isPresent()) {
              catalogoExistente.get().setProdutos(catalogo.getProdutos());
              catalogoExistente.get().setEmpresa(catalogoExistente.get().getEmpresa());

              catalogoRepository.save(catalogoExistente.get());

              return catalogoExistente.get();
         } else {
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogo não encontrado");
         }
    }

    public Catalogo getCatalogoPorIdEmpresa(String idEmpresa) {
        Optional<Catalogo> catalogoExiste = catalogoRepository.findByEmpresaUId(idEmpresa);
        if (catalogoExiste.isPresent()) {
            return catalogoExiste.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogo não encontrado");
        }
    }

    public void adicionarProduto(Produto produto, String idEmpresa) {
        Optional<Catalogo> catalogo = catalogoRepository.findByEmpresaUId(idEmpresa);
        if (catalogo.isPresent()) {
            List<Produto> listaNovaProdutos = catalogo.get().getProdutos();
            produto.setCatalogo(catalogo.get());
            listaNovaProdutos.add(produto);
            catalogo.get().setProdutos(listaNovaProdutos);
            catalogoRepository.save(catalogo.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogo não encontrado");
        }
    }

    public void removerProduto(Produto produto, String idEmpresa) {
        Optional<Catalogo> catalogo = catalogoRepository.findByEmpresaUId(idEmpresa);
        if (catalogo.isPresent()) {
            List<Produto> listaNovaProdutos = catalogo.get().getProdutos();
            listaNovaProdutos.remove(produto);
            catalogo.get().setProdutos(listaNovaProdutos);
            catalogoRepository.save(catalogo.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogo não encontrado");
        }
    }

    public void atualizarProduto(Produto produto, String idEmpresa) {
        Optional<Catalogo> catalogo = catalogoRepository.findByEmpresaUId(idEmpresa);
        if (catalogo.isPresent()) {
            List<Produto> listaNovaProdutos = catalogo.get().getProdutos();
            listaNovaProdutos.remove(produto);
            listaNovaProdutos.add(produto);
            catalogo.get().setProdutos(listaNovaProdutos);
            catalogoRepository.save(catalogo.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogo não encontrado");
        }
    }

}