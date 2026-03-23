package com.ecoloop.model;

import java.time.LocalDate;

public class Reciclagem {
    private int id;
    private int usuarioId;
    private int itemId;
    private int quantidade;
    private LocalDate data;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}

