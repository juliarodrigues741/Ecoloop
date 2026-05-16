package com.ecoloop.dao;

import com.ecoloop.model.Conquista;
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

    private final RowMapper<UsuarioConquista> mapper = (rs, rowNum) -> {
        UsuarioConquista uc = new UsuarioConquista();
        uc.setUsuarioId(rs.getInt("usuario_id"));
        uc.setConquistaId(rs.getInt("conquista_id"));

        java.sql.Timestamp ts = rs.getTimestamp("data_conquista");
        if (ts != null) uc.setDataConquista(ts.toLocalDateTime());

        Conquista c = new Conquista();
        c.setId(rs.getInt("conquista_id"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        c.setPontosRecompensa(rs.getInt("pontos_recompensa"));
        c.setNivelRequerido(rs.getString("nivel_requerido"));
        c.setImagemUrl(rs.getString("imagem_url"));
        uc.setConquista(c);

        return uc;
    };

    public List<UsuarioConquista> findByUsuario(Integer usuarioId) {
    String sql = """
        SELECT uc.usuario_id, uc.conquista_id, uc.data_conquista,
               c.titulo, c.descricao, c.pontos_recompensa, c.nivel_requerido, c.imagem_url
        FROM usuarios_conquistas uc
        INNER JOIN conquistas c ON c.id = uc.conquista_id
        WHERE uc.usuario_id = ?
        ORDER BY uc.data_conquista DESC
    """;
    return jdbc.query(sql, mapper, usuarioId);
}

    public boolean addConquista(int usuarioId, Integer conquistaId) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM usuarios_conquistas WHERE usuario_id = ? AND conquista_id = ?",
            Integer.class, usuarioId, conquistaId
        );
        if (count != null && count > 0) return false;

        jdbc.update(
            "INSERT INTO usuarios_conquistas (usuario_id, conquista_id, data_conquista) VALUES (?, ?, NOW())",
            usuarioId, conquistaId
        );
    return true;
}

    public boolean remover(Integer usuarioId, Integer conquistaId) {
        return jdbc.update(
            "DELETE FROM usuarios_conquistas WHERE usuario_id = ? AND conquista_id = ?",
            usuarioId, conquistaId
        ) > 0;
    }
}