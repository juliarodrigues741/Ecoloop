package com.ecoloop.model;

public class Desafio {

    private int id;
    private String titulo;
    private String descricao;
    private int pontosRecompensa;
    private String nivelRequerido; // Bronze, Prata, Ouro
    private String imagemUrl;
    private double metaKg; // meta de kg a reciclar
    private String tipoMaterial; // plástico, papel, vidro, geral...

    // GETTERS e SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getPontosRecompensa() { return pontosRecompensa; }
    public void setPontosRecompensa(int pontosRecompensa) { this.pontosRecompensa = pontosRecompensa; }

    public String getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(String nivelRequerido) { this.nivelRequerido = nivelRequerido; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public double getMetaKg() { return metaKg; }
    public void setMetaKg(double metaKg) { this.metaKg = metaKg; }

    public String getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(String tipoMaterial) { this.tipoMaterial = tipoMaterial; }
}
