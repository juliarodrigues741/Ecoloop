package com.ecoloop.model;

public class Beneficio {
    private Integer id;
    private String nome;
    private String descricao;
    private String categoria;
    private Integer pontos_necessarios;
    private String imagemUrl;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getPontosNecessarios() { return pontos_necessarios; }
    public void setPontosNecessarios(Integer pontos_necessarios) { this.pontos_necessarios = pontos_necessarios; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
}

