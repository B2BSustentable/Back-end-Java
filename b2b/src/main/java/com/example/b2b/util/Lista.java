package com.example.b2b.util;

import com.example.b2b.entity.empresa.Empresa;
import com.example.b2b.entity.responsavel.Responsavel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lista<T> {

    private static final int TAMANHO_PADRAO = 10; // Tamanho inicial do vetor
    private T[] vetor;
    private int nroElem;

    public Lista() {
        this(TAMANHO_PADRAO);
    }

    public Lista(int tamanho) {
        vetor = (T[]) new Object[tamanho];
        nroElem = 0;
    }

    public void adiciona(T elemento) {
        if (nroElem >= vetor.length) {
            aumentarCapacidade(); // Aumenta a capacidade do vetor se estiver cheio
        }
        vetor[nroElem++] = elemento;
    }

    public void exibe() {
        if (nroElem == 0) {
            System.out.println("\nA lista está vazia.");
        } else {
            System.out.println("\nElementos da lista:");
            for (int i = 0; i < nroElem; i++) {
                System.out.println(vetor[i]);
            }
        }
    }

    public int busca(T elementoBuscado) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].equals(elementoBuscado)) {
                return i;
            }
        }
        return -1;
    }

    public T buscaPorIndice(int i){
        return vetor[i];
    }

    public void set(int indice, T elemento) {
        if (indice < 0 || indice >= nroElem) {
            throw new IndexOutOfBoundsException("Índice fora dos limites");
        }
        vetor[indice] = elemento;
    }

    public boolean removePeloIndice(int indice) {
        if (indice < 0 || indice >= nroElem) {
            System.out.println("\nIndice inválido!");
            return false;
        }
        for (int i = indice; i < nroElem - 1; i++) {
            vetor[i] = vetor[i + 1];
        }
        nroElem--;
        return true;
    }

    public boolean removeElemento(T elementoARemover) {
        return removePeloIndice(busca(elementoARemover));
    }

    public int getTamanho() {
        return nroElem;
    }

    public T getElemento(int indice) {
        if (indice < 0 || indice >= nroElem) {
            return null;
        } else {
            return vetor[indice];
        }
    }

    public void limpa() {
        nroElem = 0;
    }

    private void aumentarCapacidade() {
        int novoTamanho = vetor.length * 2; // Dobre o tamanho do vetor
        T[] novoVetor = (T[]) new Object[novoTamanho];
        System.arraycopy(vetor, 0, novoVetor, 0, nroElem);
        vetor = novoVetor;
    }

    public List<T> toList() {
        return new ArrayList<>(Arrays.asList(Arrays.copyOf(vetor, nroElem)));
    }

    public void adicionarTodos(List<?> lista) {
        for (Object o : lista) {
            adiciona((T) o);
        }
    }


    public void selectionSortByDataDeCriacao() {
            int n = nroElem;

            for (int i = 0; i < n - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < n; j++) {
                    if (((Responsavel)buscaPorIndice(j)).getDataDeCriacaoResponsavel().isBefore(((Responsavel)buscaPorIndice(minIndex)).getDataDeCriacaoResponsavel())) {
                        minIndex = j;
                    }
                }

                if (minIndex != i) {
                    swap(i, minIndex);
                }
            }
    }

        private void swap(int i, int j) {
            T temp = buscaPorIndice(i);
            set(i, buscaPorIndice(j));
            set(j, temp);
        }

    public Empresa buscaBinariaPorDataDeCriacao(LocalDateTime data) {
        int esquerda = 0;
        int direita = nroElem - 1;

        while (esquerda <= direita) {
            int meio = (esquerda + direita) / 2;
            Empresa empresaMeio = (Empresa) buscaPorIndice(meio);
            LocalDateTime dataMeio = empresaMeio.getDataDeCriacao();
            int comparacao = data.compareTo(dataMeio);

            if (comparacao == 0) {
                return empresaMeio; // Encontrado
            } else if (comparacao < 0) {
                direita = meio - 1;
            } else {
                esquerda = meio + 1;
            }
        }

        return null;
    }





}



