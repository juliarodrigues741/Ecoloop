package com.ecoloop.model;

import java.time.LocalDateTime;

public class Resgate {
    private Integer id;
    private Integer usuarioId;
    private Integer beneficioId;
    private LocalDateTime dataResgate;
    private Integer pontosGastos;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public Integer getBeneficioId() { return beneficioId; }
    public void setBeneficioId(Integer beneficioId) { this.beneficioId = beneficioId; }
    public LocalDateTime getDataResgate() { return dataResgate; }
    public void setDataResgate(LocalDateTime dataResgate) { this.dataResgate = dataResgate; }
    public Integer getPontosGastos() { return pontosGastos; }
    public void setPontosGastos(Integer pontosGastos) { this.pontosGastos = pontosGastos; }
}
