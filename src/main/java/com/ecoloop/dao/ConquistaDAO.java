package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.ConquistaDAOInterface;
import com.ecoloop.model.Conquista;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConquistaDAO implements ConquistaDAOInterface {

    private final JdbcTemplate jdbc;

    public ConquistaDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Conquista> mapper = (rs, rowNum) -> {
        Conquista c = new Conquista();
        c.setId(rs.getInt("id"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        c.setPontosRecompensa(rs.getInt("pontos_recompensa"));
        c.setNivelRequerido(rs.getString("nivel_requerido"));
        c.setImagemUrl(rs.getString("imagem_url"));
        return c;
    };

    @Override
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

    @Override
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

    @Override
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM conquistas WHERE id=?", id) > 0;
    }

    @Override
    public Conquista findById(int id) {
        List<Conquista> list = jdbc.query(
                "SELECT * FROM conquistas WHERE id=?",
                mapper, id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Conquista> findAll() {
        return jdbc.query("SELECT * FROM conquistas ORDER BY pontos_recompensa DESC", mapper);
    }

    @Override
    public List<Conquista> findAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        String placeholders = String.join(",", ids.stream().map(id -> "?").toList());
        String sql = "SELECT * FROM conquistas WHERE id IN (" + placeholders + ")";

        return jdbc.query(sql, mapper, ids.toArray());
    }
}
