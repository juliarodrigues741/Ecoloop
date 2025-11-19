package com.ecoloop.model;

public class ConfiguracoesUsuario {
    private Integer id;
    private Integer usuarioId;
    private String idioma;
    private Boolean temaEscuro;
    private Boolean notificacaoEmail;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Boolean getTemaEscuro() { return temaEscuro; }
    public void setTemaEscuro(Boolean temaEscuro) { this.temaEscuro = temaEscuro; }
    public Boolean getNotificacaoEmail() { return notificacaoEmail; }
    public void setNotificacaoEmail(Boolean notificacaoEmail) { this.notificacaoEmail = notificacaoEmail; }
}
