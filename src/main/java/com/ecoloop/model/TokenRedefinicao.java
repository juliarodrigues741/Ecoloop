package com.ecoloop.model;

import java.time.LocalDateTime;

public class TokenRedefinicao {

    private Integer id;
    private Integer usuarioId;
    private String token;
    private LocalDateTime expiracao;
    private boolean usado;

    public TokenRedefinicao() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiracao() { return expiracao; }
    public void setExpiracao(LocalDateTime expiracao) { this.expiracao = expiracao; }

    public boolean isUsado() { return usado; }
    public void setUsado(boolean usado) { this.usado = usado; }
}