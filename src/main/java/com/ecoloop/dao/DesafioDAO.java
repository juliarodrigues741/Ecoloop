package com.ecoloop.dao;

import com.ecoloop.model.Desafio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DesafioDAO {

    private final JdbcTemplate jdbc;

    public DesafioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Desafio> mapper = (rs, rowNum) -> {
        Desafio d = new Desafio();
        d.setId(rs.getInt("id"));
        d.setTitulo(rs.getString("titulo"));
        d.setDescricao(rs.getString("descricao"));
        d.setPontosRecompensa(rs.getInt("pontos_recompensa"));
        d.setNivelRequerido(rs.getString("nivel_requerido"));
        d.setImagemUrl(rs.getString("imagem_url"));
        return d;
    };

    public void adicionarDesafio(Desafio desafio) {
        String sql = "INSERT INTO desafios (titulo, descricao, pontos_recompensa, nivel_requerido, imagem_url) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, desafio.getTitulo(), desafio.getDescricao(), desafio.getPontosRecompensa(),
                desafio.getNivelRequerido(), desafio.getImagemUrl());
    }

    public List<Desafio> listarDesafios() {
        String sql = "SELECT * FROM desafios";
        return jdbc.query(sql, mapper);
    }

    public Desafio buscarPorId(int id) {
        String sql = "SELECT * FROM desafios WHERE id=?";
        List<Desafio> list = jdbc.query(sql, mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public void atualizarDesafio(Desafio desafio) {
        String sql = "UPDATE desafios SET titulo=?, descricao=?, pontos_recompensa=?, nivel_requerido=?, imagem_url=? WHERE id=?";
        jdbc.update(sql, desafio.getTitulo(), desafio.getDescricao(), desafio.getPontosRecompensa(),
                desafio.getNivelRequerido(), desafio.getImagemUrl(), desafio.getId());
    }

    public void removerDesafio(int id) {
        String sql = "DELETE FROM desafios WHERE id=?";
        jdbc.update(sql, id);
    }
}
