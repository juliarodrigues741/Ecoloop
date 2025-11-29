package com.ecoloop.model;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioDesafio {

    private int usuarioId;
    private int desafioId;
    private LocalDateTime dataConclusao;
    
    private List<Desafio> desafios;

    // Getters e Setters
    public List<Desafio> getDesafios() {
        return desafios;
    }

    public void setDesafios(List<Desafio> desafios) {
        this.desafios = desafios;
    }

    public UsuarioDesafio() {
    }

    public UsuarioDesafio(int usuarioId, int desafioId, LocalDateTime dataConclusao) {
        this.usuarioId = usuarioId;
        this.desafioId = desafioId;
        this.dataConclusao = dataConclusao;
    }

    // Getters e Setters
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getDesafioId() {
        return desafioId;
    }

    public void setDesafioId(int desafioId) {
        this.desafioId = desafioId;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    @Override
    public String toString() {
        return "UsuarioDesafio{" +
                "usuarioId=" + usuarioId +
                ", desafioId=" + desafioId +
                ", dataConclusao=" + dataConclusao +
                '}';
    }
}
