package com.ecoloop.dao;

import com.ecoloop.model.UsuarioConquista;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioConquistaDAO {

    private final JdbcTemplate jdbc;

    public UsuarioConquistaDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<UsuarioConquista> mapper = (rs, n) -> {
        UsuarioConquista uc = new UsuarioConquista();
        uc.setUsuarioId(rs.getInt("usuario_id"));
        uc.setConquistaId(rs.getInt("conquista_id"));
        uc.setDataConquista(rs.getTimestamp("data_conquista").toLocalDateTime());
        return uc;
    };

    public boolean addConquista(int usuarioId, int conquistaId) {
        return jdbc.update("""
            INSERT INTO usuarios_conquistas (usuario_id, conquista_id)
            VALUES (?, ?)
        """, usuarioId, conquistaId) > 0;
    }

    public boolean removeConquista(int usuarioId, int conquistaId) {
        return jdbc.update("""
            DELETE FROM usuarios_conquistas 
            WHERE usuario_id=? AND conquista_id=?
        """, usuarioId, conquistaId) > 0;
    }

    public List<UsuarioConquista> findByUsuario(int usuarioId) {
        return jdbc.query("""
            SELECT *
            FROM usuarios_conquistas
            WHERE usuario_id=?
            ORDER BY data_conquista DESC
        """, mapper, usuarioId);
    }
}
