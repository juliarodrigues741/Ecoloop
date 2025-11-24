package com.ecoloop.dao;

import com.ecoloop.model.LogAcao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogAcaoDAO {

    private final JdbcTemplate jdbc;

    public LogAcaoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<LogAcao> mapper = (rs, n) -> {
        LogAcao l = new LogAcao();
        l.setId(rs.getInt("id"));
        l.setUsuarioId(rs.getObject("usuario_id") != null ? rs.getInt("usuario_id") : null);
        l.setAcao(rs.getString("acao"));
        l.setDescricao(rs.getString("descricao"));
        l.setDataAcao(rs.getTimestamp("data_acao").toLocalDateTime());
        return l;
    };

    public Integer create(LogAcao l) {
        String sql = """
            INSERT INTO logs_acoes (usuario_id, acao, descricao)
            VALUES (?, ?, ?)
        """;

        jdbc.update(sql,
                l.getUsuarioId(),
                l.getAcao(),
                l.getDescricao()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public List<LogAcao> findAll(int limit) {
        return jdbc.query(
                "SELECT * FROM logs_acoes ORDER BY data_acao DESC LIMIT ?",
                mapper,
                limit
        );
    }
}
