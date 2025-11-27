package com.ecoloop.dao;

import com.ecoloop.model.RankingLocal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingLocalDAO {

    private final JdbcTemplate jdbc;

    public RankingLocalDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<RankingLocal> mapper = (rs, n) -> {
        RankingLocal r = new RankingLocal();
        r.setId(rs.getInt("id"));
        r.setUsuarioId(rs.getInt("usuario_id"));
        r.setPosicao(rs.getInt("posicao"));
        r.setPontos(rs.getInt("pontos"));
        return r;
    };

    public Integer create(RankingLocal r) {
        String sql = """
            INSERT INTO ranking_local (usuario_id, posicao, pontos)
            VALUES (?, ?, ?)
        """;

        jdbc.update(sql,
                r.getUsuarioId(),
                r.getPosicao(),
                r.getPontos()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public boolean update(RankingLocal r) {
        String sql = """
            UPDATE ranking_local SET usuario_id=?, posicao=?, pontos=? WHERE id=?
        """;

        return jdbc.update(sql,
                r.getUsuarioId(),
                r.getPosicao(),
                r.getPontos(),
                r.getId()
        ) > 0;
    }

    public RankingLocal findByUsuarioId(int usuarioId) {
        List<RankingLocal> list = jdbc.query(
                "SELECT * FROM ranking_local WHERE usuario_id=?",
                mapper,
                usuarioId
        );

        return list.isEmpty() ? null : list.get(0);
    }

    public List<RankingLocal> topN(int n) {
        return jdbc.query(
                "SELECT * FROM ranking_local ORDER BY pontos DESC LIMIT ?",
                mapper,
                n
        );
    }

    public boolean delete(int id) {
        return jdbc.update("DELETE FROM ranking_local WHERE id=?", id) > 0;
    }
}
