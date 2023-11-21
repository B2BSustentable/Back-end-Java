package com.example.b2b.util;

import com.example.b2b.entity.empresa.Empresa;

public class UsuarioSorter {

    public static void selectionSortByDataDeCriacao(Lista<Empresa> usuarios) {
        int n = usuarios.getTamanho();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (usuarios.buscaPorIndice(j).getDataDeCriacao().isBefore(usuarios.buscaPorIndice(minIndex).getDataDeCriacao())) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                swap(usuarios, i, minIndex);
            }
        }
    }

    private static void swap(Lista<Empresa> usuarios, int i, int j) {
        Empresa temp = usuarios.buscaPorIndice(i);
        usuarios.set(i, usuarios.buscaPorIndice(j));
        usuarios.set(j, temp);
    }
}

