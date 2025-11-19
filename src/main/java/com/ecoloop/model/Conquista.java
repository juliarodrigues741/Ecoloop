package com.ecoloop.model;

public class Conquista {
    private Integer id;
    private String titulo;
    private String descricao;
    private Integer pontosRecompensa;
    private String nivelRequerido;
    private String imagemUrl;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getPontosRecompensa() { return pontosRecompensa; }
    public void setPontosRecompensa(Integer pontosRecompensa) { this.pontosRecompensa = pontosRecompensa; }
    public String getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(String nivelRequerido) { this.nivelRequerido = nivelRequerido; }
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
}
