package com.ecoloop.model;

public class ConfiguracoesUsuario {

    private Integer id;
    private String idioma;
    private boolean temaEscuro;
    private boolean notificacaoEmail;

    // 🔥 Campo que estava faltando
    private Usuario usuario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public boolean isTemaEscuro() { return temaEscuro; }
    public void setTemaEscuro(boolean temaEscuro) { this.temaEscuro = temaEscuro; }

    public boolean isNotificacaoEmail() { return notificacaoEmail; }
    public void setNotificacaoEmail(boolean notificacaoEmail) { this.notificacaoEmail = notificacaoEmail; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
