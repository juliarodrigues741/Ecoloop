package com.ecoloop.model;

import java.time.LocalDateTime;

public class UsuarioConquista {

    private Integer usuarioId;
    private Integer conquistaId;
    private LocalDateTime dataConquista;
    private Conquista conquista; // ADICIONADO: objeto completo populado via JOIN

    public UsuarioConquista() {}

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getConquistaId() { return conquistaId; }
    public void setConquistaId(Integer conquistaId) { this.conquistaId = conquistaId; }

    public LocalDateTime getDataConquista() { return dataConquista; }
    public void setDataConquista(LocalDateTime dataConquista) { this.dataConquista = dataConquista; }

    public Conquista getConquista() { return conquista; }
    public void setConquista(Conquista conquista) { this.conquista = conquista; }
}