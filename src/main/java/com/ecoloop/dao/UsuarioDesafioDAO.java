package com.ecoloop.dao;

import com.ecoloop.model.UsuarioDesafio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioDesafioDAO {

    private final JdbcTemplate jdbc;

    public UsuarioDesafioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<UsuarioDesafio> mapper = (rs, rowNum) -> {
        UsuarioDesafio ud = new UsuarioDesafio();
        ud.setUsuarioId(rs.getInt("usuario_id"));
        ud.setDesafioId(rs.getInt("desafio_id"));
        ud.setDataConclusao(rs.getTimestamp("data_conclusao").toLocalDateTime());
        return ud;
    };

    public void adicionarUsuarioDesafio(int usuarioId, int desafioId, java.time.LocalDateTime dataConclusao) {
        String sql = "INSERT INTO usuario_desafios (usuario_id, desafio_id, data_conclusao) VALUES (?, ?, ?)";
        jdbc.update(sql, usuarioId, desafioId, java.sql.Timestamp.valueOf(dataConclusao));
    }

    public List<UsuarioDesafio> listarDesafiosPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM usuario_desafios WHERE usuario_id=?";
        return jdbc.query(sql, mapper, usuarioId);
    }

    public void removerUsuarioDesafio(int usuarioId, int desafioId) {
        String sql = "DELETE FROM usuario_desafios WHERE usuario_id=? AND desafio_id=?";
        jdbc.update(sql, usuarioId, desafioId);
    }
}
