package com.ecoloop.model;

import java.time.LocalDateTime;

public class MaterialEnviado {

    private Integer id;
    private Integer usuarioId;
    private String descricao;
    private String tipoArquivo; // 'foto' | 'video'
    private String caminhoArquivo;
    private LocalDateTime dataEnvio;
    private Integer pontosGerados;
    private String status; // 'pendente', 'aprovado', 'recusado'
    private LocalDateTime dataAvaliacao;
    private String comentarioAvaliacao;

    // ➕ Campos adicionados na tabela
    private Double pesoKg; // Peso do material reciclado
    private String tipoMaterial; // Ex: plástico, papel, vidro etc.

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }

    public Integer getPontosGerados() { return pontosGerados; }
    public void setPontosGerados(Integer pontosGerados) { this.pontosGerados = pontosGerados; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }

    public String getComentarioAvaliacao() { return comentarioAvaliacao; }
    public void setComentarioAvaliacao(String comentarioAvaliacao) { this.comentarioAvaliacao = comentarioAvaliacao; }

    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }

    public String getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(String tipoMaterial) { this.tipoMaterial = tipoMaterial; }
}
