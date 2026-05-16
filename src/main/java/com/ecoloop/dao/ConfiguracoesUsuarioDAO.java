package com.ecoloop.dao;

import com.ecoloop.model.ConfiguracoesUsuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfiguracoesUsuarioDAO {

    private final JdbcTemplate jdbc;

    public ConfiguracoesUsuarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<ConfiguracoesUsuario> mapper = (rs, rowNum) -> {
        ConfiguracoesUsuario c = new ConfiguracoesUsuario();
        c.setId(rs.getInt("id"));
        c.setUsuarioId(rs.getInt("usuario_id"));
        c.setIdioma(rs.getString("idioma"));
        c.setTemaEscuro(rs.getObject("tema_escuro") != null ? rs.getBoolean("tema_escuro") : false);
        c.setNotificacaoEmail(rs.getObject("notificacao_email") != null ? rs.getBoolean("notificacao_email") : false);
        return c;
    };

    public Integer create(ConfiguracoesUsuario c) {
        String sql = """
            INSERT INTO configuracoes_usuario
            (usuario_id, idioma, tema_escuro, notificacao_email)
            VALUES (?, ?, ?, ?)
        """;

        jdbc.update(sql,
                c.getUsuarioId(),
                c.getIdioma() != null ? c.getIdioma() : "pt-BR",
                c.getTemaEscuro() != null ? c.getTemaEscuro() : false,
                c.getNotificacaoEmail() != null ? c.getNotificacaoEmail() : false
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public boolean update(ConfiguracoesUsuario c) {
        String sql = """
            UPDATE configuracoes_usuario
            SET idioma=?, tema_escuro=?, notificacao_email=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                c.getIdioma() != null ? c.getIdioma() : "pt-BR",
                c.getTemaEscuro() != null ? c.getTemaEscuro() : false,
                c.getNotificacaoEmail() != null ? c.getNotificacaoEmail() : false,
                c.getId()
        ) > 0;
    }

    public boolean saveOrUpdate(ConfiguracoesUsuario c) {
        ConfiguracoesUsuario existente = findByUsuarioId(c.getUsuarioId());

        if (existente == null) {
            return create(c) != null;
        }

        c.setId(existente.getId());
        return update(c);
    }

    public boolean delete(int id) {
        return jdbc.update(
                "DELETE FROM configuracoes_usuario WHERE id=?",
                id
        ) > 0;
    }

    public ConfiguracoesUsuario findByUsuarioId(int usuarioId) {
        List<ConfiguracoesUsuario> list = jdbc.query(
                "SELECT * FROM configuracoes_usuario WHERE usuario_id=?",
                mapper,
                usuarioId
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public List<ConfiguracoesUsuario> findAll() {
        return jdbc.query(
                "SELECT * FROM configuracoes_usuario ORDER BY id DESC",
                mapper
        );
    }
}