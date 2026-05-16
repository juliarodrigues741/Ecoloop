package com.ecoloop.model;

import java.time.LocalDateTime;

public class BeneficioResgatado {

    private Integer id;
    private Integer usuarioId;
    private Integer beneficioId;
    private LocalDateTime dataResgate;

    public BeneficioResgatado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getBeneficioId() {
        return beneficioId;
    }

    public void setBeneficioId(Integer beneficioId) {
        this.beneficioId = beneficioId;
    }

    public LocalDateTime getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDateTime dataResgate) {
        this.dataResgate = dataResgate;
    }
}