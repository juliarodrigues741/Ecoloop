package com.ecoloop.dao;

import com.ecoloop.model.ConquistaDoUsuario;
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

    private final RowMapper<UsuarioConquista> mapper = (rs, n) -> {
        UsuarioConquista uc = new UsuarioConquista();
        uc.setUsuarioId(rs.getInt("usuario_id"));
        uc.setConquistaId(rs.getInt("conquista_id"));
        uc.setDataConquista(rs.getTimestamp("data_conquista").toLocalDateTime());
        return uc;
    };

    // Listar IDs de conquistas de um usuário
    public List<Integer> listarIdsConquistasPorUsuario(int usuarioId) {
        String sql = "SELECT conquista_id FROM usuarios_conquistas WHERE usuario_id=?";
        return jdbc.queryForList(sql, Integer.class, usuarioId);
    }

    // Adicionar conquista a um usuário
    public boolean addConquista(int usuarioId, int conquistaId) {
        String sql = """
            INSERT INTO usuarios_conquistas (usuario_id, conquista_id)
            VALUES (?, ?)
        """;
        return jdbc.update(sql, usuarioId, conquistaId) > 0;
    }

    // Remover conquista de um usuário
    public boolean removeConquista(int usuarioId, int conquistaId) {
        String sql = """
            DELETE FROM usuarios_conquistas 
            WHERE usuario_id=? AND conquista_id=?
        """;
        return jdbc.update(sql, usuarioId, conquistaId) > 0;
    }

    // Buscar conquistas completas de um usuário
    public List<UsuarioConquista> findByUsuario(int usuarioId) {
        String sql = """
            SELECT *
            FROM usuarios_conquistas
            WHERE usuario_id=?
            ORDER BY data_conquista DESC
        """;
        return jdbc.query(sql, mapper, usuarioId);
    }

    // Listar IDs de conquistas de forma alternativa
    public List<Integer> findConquistaIdsByUsuario(int usuarioId) {
        String sql = """
            SELECT conquista_id
            FROM usuarios_conquistas
            WHERE usuario_id=?
        """;
        return jdbc.query(sql, (rs, n) -> rs.getInt("conquista_id"), usuarioId);
    }

    // Deletar todas conquistas de um usuário
    public void deleteAllByUsuario(int usuarioId) {
        String sql = """
            DELETE FROM usuarios_conquistas
            WHERE usuario_id=?
        """;
        jdbc.update(sql, usuarioId);
    }

    // Buscar conquistas completas com detalhes
    public List<ConquistaDoUsuario> findConquistasCompletasByUsuario(int usuarioId) {
        String sql = """
            SELECT c.id, c.titulo, c.descricao, c.imagem_url,
                   c.pontos_recompensa, c.nivel_requerido,
                   uc.data_conquista
            FROM usuarios_conquistas uc
            JOIN conquistas c ON c.id = uc.conquista_id
            WHERE uc.usuario_id = ?
            ORDER BY uc.data_conquista DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            ConquistaDoUsuario dto = new ConquistaDoUsuario();
            dto.setId(rs.getInt("id"));
            dto.setTitulo(rs.getString("titulo"));
            dto.setDescricao(rs.getString("descricao"));
            dto.setImagemUrl(rs.getString("imagem_url"));
            dto.setPontosRecompensa(rs.getInt("pontos_recompensa"));
            dto.setNivelRequerido(rs.getString("nivel_requerido"));
            dto.setDataConquista(rs.getTimestamp("data_conquista").toLocalDateTime());
            return dto;
        }, usuarioId);
    }
}
