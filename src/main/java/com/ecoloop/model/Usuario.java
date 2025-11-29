package com.ecoloop.model;

import java.time.LocalDateTime;
import java.util.List;

public class Usuario {

    private Integer id;
    private String nome;
    private String email;
    private String senhaHash;
    private String fotoPerfil;
    private String nivel;
    private Integer pontos;
    private LocalDateTime dataCadastro;
    private String role;
    private Double totalKg;
    // Já existente

    // 🔥 ADICIONADO — USADO NO DASHBOARD
    private Double metaMensal = 50.0;             // exemplo: 50kg
    private Double metaMensalProgresso = 0.0;     // quanto já fez
    private Double metaMensalPercentual = 0.0;    // % para barra de progresso
    private List<Desafio> desafios;
    private Conquista conquistaPrincipal;
    private int totalUploadsAprovados;
    private double co2; // ou int, dependendo do que você precisa
    private List<Arvore> arvores; // ou outro tipo adequado

    public List<Arvore> getArvores() {
        return arvores;
    }

    public void setArvores(List<Arvore> arvores) {
        this.arvores = arvores;
    }

    // Getter público
    public double getCo2() {
        return co2;
    }

    // Setter público (opcional)
    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public int getTotalUploadsAprovados() {
        return totalUploadsAprovados;
    }

    public void setTotalUploadsAprovados(int totalUploadsAprovados) {
        this.totalUploadsAprovados = totalUploadsAprovados;
    }

    public Conquista getConquistaPrincipal() { return conquistaPrincipal; }

    public void setConquistaPrincipal(Conquista conquistaPrincipal) { this.conquistaPrincipal = conquistaPrincipal; }

    public void setDesafios(List<Desafio> desafios) {
        this.desafios = desafios;
    }

    public List<Desafio> getDesafios() {
        return desafios;
    }


    // getters e setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Integer getPontos() { return pontos; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Double getTotalKg() { return totalKg; }
    public void setTotalKg(Double totalKg) { this.totalKg = totalKg; }

    // 👇 ADICIONADOS
    public Double getMetaMensal() { return metaMensal; }
    public void setMetaMensal(Double metaMensal) { this.metaMensal = metaMensal; }

    public Double getMetaMensalProgresso() { return metaMensalProgresso; }
    public void setMetaMensalProgresso(Double metaMensalProgresso) { this.metaMensalProgresso = metaMensalProgresso; }

    public Double getMetaMensalPercentual() { return metaMensalPercentual; }
    public void setMetaMensalPercentual(Double metaMensalPercentual) { this.metaMensalPercentual = metaMensalPercentual; }
}
