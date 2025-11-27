package com.ecoloop.model;

import java.time.LocalDateTime;

public class ConquistaDoUsuario {
    private int id;
    private String titulo;
    private String descricao;
    private String imagemUrl;
    private int pontosRecompensa;
    private String nivelRequerido;
    private LocalDateTime dataConquista;

    // getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public int getPontosRecompensa() { return pontosRecompensa; }
    public void setPontosRecompensa(int pontosRecompensa) { this.pontosRecompensa = pontosRecompensa; }

    public String getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(String nivelRequerido) { this.nivelRequerido = nivelRequerido; }

    public LocalDateTime getDataConquista() { return dataConquista; }
    public void setDataConquista(LocalDateTime dataConquista) { this.dataConquista = dataConquista; }
}
