package com.ecoloop.dao;

import com.ecoloop.model.MaterialEnviado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MaterialEnviadoDAO {

    private final JdbcTemplate jdbc;

    public MaterialEnviadoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<MaterialEnviado> mapper = (rs, n) -> {
        MaterialEnviado m = new MaterialEnviado();
        m.setId(rs.getInt("id"));
        m.setUsuarioId(rs.getInt("usuario_id"));
        m.setDescricao(rs.getString("descricao"));
        m.setTipoArquivo(rs.getString("tipo_arquivo"));
        m.setCaminhoArquivo(rs.getString("caminho_arquivo"));

        if (rs.getTimestamp("data_envio") != null)
            m.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());

        m.setPontosGerados(rs.getInt("pontos_gerados"));
        m.setStatus(rs.getString("status"));

        if (rs.getTimestamp("data_avaliacao") != null)
            m.setDataAvaliacao(rs.getTimestamp("data_avaliacao").toLocalDateTime());

        m.setComentarioAvaliacao(rs.getString("comentario_avaliacao"));
        return m;
    };

    // ===========================
    // CRIAÇÃO
    // ===========================
    public Integer create(MaterialEnviado m) {
        String sql = """
                INSERT INTO materiais_enviados
                (usuario_id, descricao, tipo_arquivo, caminho_arquivo, pontos_gerados, status, comentario_avaliacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbc.update(sql,
                m.getUsuarioId(),
                m.getDescricao(),
                m.getTipoArquivo(),
                m.getCaminhoArquivo(),
                m.getPontosGerados(),
                m.getStatus(),
                m.getComentarioAvaliacao()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // ===========================
    // UPDATE GERAL
    // ===========================
    public boolean update(MaterialEnviado m) {
        String sql = """
                UPDATE materiais_enviados SET
                descricao=?, tipo_arquivo=?, caminho_arquivo=?, pontos_gerados=?,
                status=?, data_avaliacao=?, comentario_avaliacao=?
                WHERE id=?
                """;

        return jdbc.update(sql,
                m.getDescricao(),
                m.getTipoArquivo(),
                m.getCaminhoArquivo(),
                m.getPontosGerados(),
                m.getStatus(),
                m.getDataAvaliacao(),
                m.getComentarioAvaliacao(),
                m.getId()
        ) > 0;
    }

    // ===========================
    // BUSCAS
    // ===========================
    public MaterialEnviado findById(int id) {
        List<MaterialEnviado> list = jdbc.query(
                "SELECT * FROM materiais_enviados WHERE id=?",
                mapper,
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public List<MaterialEnviado> findByUsuarioId(int usuarioId) {
        return jdbc.query(
                "SELECT * FROM materiais_enviados WHERE usuario_id=? ORDER BY data_envio DESC",
                mapper, usuarioId
        );
    }

    public List<MaterialEnviado> findAll() {
        return jdbc.query("SELECT * FROM materiais_enviados ORDER BY data_envio DESC", mapper);
    }

    public List<MaterialEnviado> findAll(int limit, int offset) {
        return jdbc.query(
                "SELECT * FROM materiais_enviados ORDER BY data_envio DESC LIMIT ? OFFSET ?",
                mapper, limit, offset
        );
    }

    // ===========================
    // DELETE
    // ===========================
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM materiais_enviados WHERE id=?", id) > 0;
    }

    // ===========================
    // ADMIN — PENDENTES
    // ===========================
    public int countPendentes() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM materiais_enviados WHERE status='pendente'",
                Integer.class
        );
    }

    public List<MaterialEnviado> findAllPendentes() {
        return jdbc.query(
                "SELECT * FROM materiais_enviados WHERE status='pendente' ORDER BY data_envio ASC",
                mapper
        );
    }

    // ===========================
    // ADMIN — APROVAR MATERIAL
    // ===========================
    public boolean aprovar(int id, int pontosGerados) {
        String sql = """
                UPDATE materiais_enviados SET
                status='aprovado',
                pontos_gerados=?,
                data_avaliacao=?,
                comentario_avaliacao=NULL
                WHERE id=?
                """;

        return jdbc.update(sql,
                pontosGerados,
                LocalDateTime.now(),
                id
        ) > 0;
    }

    // ===========================
    // ADMIN — REPROVAR MATERIAL
    // ===========================
    public boolean reprovar(int id, String comentario) {
        String sql = """
                UPDATE materiais_enviados SET
                status='recusado',
                comentario_avaliacao=?,
                data_avaliacao=?
                WHERE id=?
                """;

        return jdbc.update(sql,
                comentario,
                LocalDateTime.now(),
                id
        ) > 0;
    }
    
    public boolean aprovar(int id, int pontos, String comentario) {
        String sql = """
            UPDATE materiais_enviados
            SET status='aprovado',
                pontos_gerados=?,
                comentario_avaliacao=?,
                data_avaliacao=NOW()
            WHERE id=?
        """;
        return jdbc.update(sql, pontos, comentario, id) > 0;
    }

    public boolean recusar(int id, String comentario) {
        String sql = """
            UPDATE materiais_enviados
            SET status='recusado',
                comentario_avaliacao=?,
                data_avaliacao=NOW()
            WHERE id=?
        """;
        return jdbc.update(sql, comentario, id) > 0;
    }

}
