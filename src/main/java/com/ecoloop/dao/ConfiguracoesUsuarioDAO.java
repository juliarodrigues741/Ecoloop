package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.ConfiguracoesUsuarioDAOInterface;
import com.ecoloop.model.ConfiguracoesUsuario;
import com.ecoloop.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfiguracoesUsuarioDAO implements ConfiguracoesUsuarioDAOInterface {

    private final JdbcTemplate jdbc;

    public ConfiguracoesUsuarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<ConfiguracoesUsuario> mapper = (rs, rowNum) -> {
        ConfiguracoesUsuario c = new ConfiguracoesUsuario();
        c.setId(rs.getInt("id"));

        Usuario u = new Usuario();
        u.setId(rs.getInt("usuario_id"));
        c.setUsuario(u);

        c.setIdioma(rs.getString("idioma"));
        c.setTemaEscuro(rs.getBoolean("tema_escuro"));
        c.setNotificacaoEmail(rs.getBoolean("notificacao_email"));
        return c;
    };

    @Override
    public Integer create(ConfiguracoesUsuario c) {
        String sql = """
            INSERT INTO configuracoes_usuario (usuario_id, idioma, tema_escuro, notificacao_email)
            VALUES (?, ?, ?, ?)
        """;

        jdbc.update(sql,
                c.getUsuario().getId(),
                c.getIdioma(),
                c.isTemaEscuro(),
                c.isNotificacaoEmail()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    @Override
    public boolean update(ConfiguracoesUsuario c) {
        String sql = """
            UPDATE configuracoes_usuario SET idioma=?, tema_escuro=?, notificacao_email=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                c.getIdioma(),
                c.isTemaEscuro(),
                c.isNotificacaoEmail(),
                c.getId()
        ) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM configuracoes_usuario WHERE id=?", id) > 0;
    }

    @Override
    public ConfiguracoesUsuario findByUsuarioId(int usuarioId) {
        List<ConfiguracoesUsuario> list = jdbc.query(
                "SELECT * FROM configuracoes_usuario WHERE usuario_id=?",
                mapper, usuarioId
        );
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ConfiguracoesUsuario> findAll() {
        return jdbc.query("SELECT * FROM configuracoes_usuario", mapper);
    }

    @Override
    public void saveOrUpdate(ConfiguracoesUsuario c) {
        if (c.getId() == null) {
            c.setId(create(c));
        } else {
            update(c);
        }
    }
}
