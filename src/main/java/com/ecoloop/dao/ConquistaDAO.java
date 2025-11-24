package com.ecoloop.dao;

import com.ecoloop.model.Conquista;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConquistaDAO {

    private final JdbcTemplate jdbc;

    public ConquistaDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<Conquista> mapper = (rs, rowNum) -> {
        Conquista c = new Conquista();
        c.setId(rs.getInt("id"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        c.setPontosRecompensa(rs.getInt("pontos_recompensa"));
        c.setNivelRequerido(rs.getString("nivel_requerido"));
        c.setImagemUrl(rs.getString("imagem_url"));
        return c;
    };

    public Integer create(Conquista c) {
        String sql = """
            INSERT INTO conquistas
            (titulo, descricao, pontos_recompensa, nivel_requerido, imagem_url)
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                c.getTitulo(),
                c.getDescricao(),
                c.getPontosRecompensa(),
                c.getNivelRequerido(),
                c.getImagemUrl()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public boolean update(Conquista c) {
        String sql = """
            UPDATE conquistas SET
            titulo=?, descricao=?, pontos_recompensa=?, nivel_requerido=?, imagem_url=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                c.getTitulo(),
                c.getDescricao(),
                c.getPontosRecompensa(),
                c.getNivelRequerido(),
                c.getImagemUrl(),
                c.getId()
        ) > 0;
    }

    public boolean delete(int id) {
        return jdbc.update("DELETE FROM conquistas WHERE id=?", id) > 0;
    }

    public Conquista findById(int id) {
        List<Conquista> list = jdbc.query(
                "SELECT * FROM conquistas WHERE id=?",
                mapper, id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Conquista> findAll() {
        return jdbc.query("SELECT * FROM conquistas ORDER BY pontos_recompensa DESC", mapper);
    }
}
