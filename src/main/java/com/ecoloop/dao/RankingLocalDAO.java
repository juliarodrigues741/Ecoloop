package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.RankingLocalDAOInterface;
import com.ecoloop.model.RankingLocal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingLocalDAO implements RankingLocalDAOInterface {

    private final JdbcTemplate jdbc;

    public RankingLocalDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<RankingLocal> mapper = (rs, n) -> {
        RankingLocal r = new RankingLocal();
        r.setId(rs.getInt("id"));
        r.setUsuarioId(rs.getInt("usuario_id"));
        r.setPontos(rs.getInt("pontos"));
        r.setPosicao(null); // Não vem do banco

        r.setNomeUsuario(rs.getString("nome_usuario"));
        return r;
    };

    @Override
    public List<RankingLocal> getRankingLocal() {

        String sql = """
            SELECT 
                r.id,
                r.usuario_id,
                r.pontos,
                u.nome AS nome_usuario
            FROM ranking_local r
            JOIN usuarios u ON u.id = r.usuario_id
            ORDER BY r.pontos DESC
        """;

        return jdbc.query(sql, mapper);
    }

    @Override
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

    @Override
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

    @Override
    public RankingLocal findByUsuarioId(int usuarioId) {

        String sql = """
            SELECT 
                r.id,
                r.usuario_id,
                r.pontos,
                u.nome AS nome_usuario
            FROM ranking_local r
            JOIN usuarios u ON u.id = r.usuario_id
            WHERE r.usuario_id = ?
        """;

        List<RankingLocal> list = jdbc.query(sql, mapper, usuarioId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<RankingLocal> topN(int n) {

        String sql = """
            SELECT 
                r.id,
                r.usuario_id,
                r.pontos,
                u.nome AS nome_usuario
            FROM ranking_local r
            JOIN usuarios u ON u.id = r.usuario_id
            ORDER BY r.pontos DESC
            LIMIT ?
        """;

        return jdbc.query(sql, mapper, n);
    }

    @Override
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM ranking_local WHERE id=?", id) > 0;
    }
}
