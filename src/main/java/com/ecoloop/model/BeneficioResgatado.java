package com.ecoloop.model;

import java.time.LocalDateTime;

public class BeneficioResgatado {

    private Integer id;
    private Integer usuarioId;
    private Integer beneficioId;
    private LocalDateTime dataResgate;

    // Campos carregados do benefício
    private String nome;
    private String categoria;
    private String descricao;
    private Integer pontos_necessarios;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getBeneficioId() { return beneficioId; }
    public void setBeneficioId(Integer beneficioId) { this.beneficioId = beneficioId; }

    public LocalDateTime getDataResgate() { return dataResgate; }
    public void setDataResgate(LocalDateTime dataResgate) { this.dataResgate = dataResgate; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getPontos() { return pontos_necessarios; }
    public void setPontos(Integer pontos) { this.pontos_necessarios = pontos; }
}
