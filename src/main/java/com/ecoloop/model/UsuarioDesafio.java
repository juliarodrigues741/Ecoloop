package com.ecoloop.model;

import java.time.LocalDateTime;

public class UsuarioDesafio {

    private int usuarioId;
    private int desafioId;
    private LocalDateTime dataConclusao;
    
    private double progresso; // progresso do usuário
    private Desafio desafio; // o desafio completo

    // GETTERS E SETTERS

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getDesafioId() { return desafioId; }
    public void setDesafioId(int desafioId) { this.desafioId = desafioId; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public double getProgresso() { return progresso; }
    public void setProgresso(double progresso) { this.progresso = progresso; }

    public Desafio getDesafio() { return desafio; }
    public void setDesafio(Desafio desafio) { this.desafio = desafio; }
}
