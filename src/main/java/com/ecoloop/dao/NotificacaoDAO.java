package com.ecoloop.dao;

import com.ecoloop.model.Notificacao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificacaoDAO {

    private final JdbcTemplate jdbc;

    public NotificacaoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<Notificacao> mapper = (rs, n) -> {
        Notificacao no = new Notificacao();
        no.setId(rs.getInt("id"));
        no.setUsuarioId(rs.getInt("usuario_id"));
        no.setMensagem(rs.getString("mensagem"));
        no.setTipo(rs.getString("tipo"));
        no.setLida(rs.getBoolean("lida"));
        if (rs.getTimestamp("data_envio") != null)
            no.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());
        return no;
    };

    // ===========================
    // CREATE
    // ===========================
    public Integer create(Notificacao n) {
        String sql = """
            INSERT INTO notificacoes (usuario_id, mensagem, tipo, lida, data_envio)
            VALUES (?, ?, ?, ?, ?)
        """;
        jdbc.update(sql,
                n.getUsuarioId(),
                n.getMensagem(),
                n.getTipo(),
                n.getLida(),
                n.getDataEnvio()
        );
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // ===========================
    // FINDERS
    // ===========================
    public List<Notificacao> findByUsuario(int usuarioId) {
        return jdbc.query(
                "SELECT * FROM notificacoes WHERE usuario_id=? ORDER BY data_envio DESC",
                mapper, usuarioId
        );
    }

    // ===========================
    // UPDATE
    // ===========================
    public boolean marcarComoLida(int id) {
        return jdbc.update("UPDATE notificacoes SET lida=1 WHERE id=?", id) > 0;
    }

    // ===========================
    // DELETE
    // ===========================
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM notificacoes WHERE id=?", id) > 0;
    }
}
