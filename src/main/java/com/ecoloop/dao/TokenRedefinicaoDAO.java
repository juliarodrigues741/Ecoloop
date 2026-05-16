package com.ecoloop.dao;

import com.ecoloop.model.TokenRedefinicao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class TokenRedefinicaoDAO {

    private final JdbcTemplate jdbc;

    public TokenRedefinicaoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<TokenRedefinicao> mapper = (rs, rowNum) -> {
        TokenRedefinicao t = new TokenRedefinicao();
        t.setId(rs.getInt("id"));
        t.setUsuarioId(rs.getInt("usuario_id"));
        t.setToken(rs.getString("token"));
        t.setUsado(rs.getBoolean("usado"));
        Timestamp ts = rs.getTimestamp("expiracao");
        if (ts != null) t.setExpiracao(ts.toLocalDateTime());
        return t;
    };

    public void salvar(TokenRedefinicao token) {
        jdbc.update(
            "INSERT INTO tokens_redefinicao (usuario_id, token, expiracao, usado) VALUES (?, ?, ?, ?)",
            token.getUsuarioId(),
            token.getToken(),
            Timestamp.valueOf(token.getExpiracao()),
            false
        );
    }

    public TokenRedefinicao findByToken(String token) {
        List<TokenRedefinicao> list = jdbc.query(
            "SELECT * FROM tokens_redefinicao WHERE token = ?",
            mapper, token
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public void marcarComoUsado(String token) {
        jdbc.update("UPDATE tokens_redefinicao SET usado = true WHERE token = ?", token);
    }

    public void deletarExpirados() {
        jdbc.update("DELETE FROM tokens_redefinicao WHERE expiracao < NOW() OR usado = true");
    }
}