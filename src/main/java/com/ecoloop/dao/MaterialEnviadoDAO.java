package com.ecoloop.dao;

import com.ecoloop.model.MaterialEnviado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MaterialEnviadoDAO {

    private final JdbcTemplate jdbc;

    public MaterialEnviadoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<MaterialEnviado> mapper = (rs, n) -> {
        MaterialEnviado m = new MaterialEnviado();
        m.setId(rs.getInt("id"));
        m.setUsuarioId(rs.getInt("usuario_id"));
        m.setDescricao(rs.getString("descricao"));
        m.setMaterial(rs.getString("material"));
        m.setTipoArquivo(rs.getString("tipo_arquivo"));
        m.setCaminhoArquivo(rs.getString("caminho_arquivo"));

        Timestamp dataEnvio = rs.getTimestamp("data_envio");
        if (dataEnvio != null) {
            m.setDataEnvio(dataEnvio.toLocalDateTime());
        }

        m.setPontosGerados(rs.getInt("pontos_gerados"));
        m.setStatus(rs.getString("status"));

        Timestamp dataAvaliacao = rs.getTimestamp("data_avaliacao");
        if (dataAvaliacao != null) {
            m.setDataAvaliacao(dataAvaliacao.toLocalDateTime());
        }

        m.setComentarioAvaliacao(rs.getString("comentario_avaliacao"));
        return m;
    };

    public Integer create(MaterialEnviado m) {
        String sql = """
            INSERT INTO materiais_enviados
            (usuario_id, descricao, material, tipo_arquivo, caminho_arquivo, pontos_gerados, status, comentario_avaliacao)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                m.getUsuarioId(),
                m.getDescricao(),
                m.getMaterial(),
                m.getTipoArquivo(),
                m.getCaminhoArquivo(),
                m.getPontosGerados(),
                m.getStatus(),
                m.getComentarioAvaliacao()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public boolean update(MaterialEnviado m) {
        String sql = """
            UPDATE materiais_enviados
            SET descricao=?, material=?, tipo_arquivo=?, caminho_arquivo=?, pontos_gerados=?,
                status=?, data_avaliacao=?, comentario_avaliacao=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                m.getDescricao(),
                m.getMaterial(),
                m.getTipoArquivo(),
                m.getCaminhoArquivo(),
                m.getPontosGerados(),
                m.getStatus(),
                m.getDataAvaliacao(),
                m.getComentarioAvaliacao(),
                m.getId()
        ) > 0;
    }

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
                mapper,
                usuarioId
        );
    }

    public List<MaterialEnviado> findAll() {
        return jdbc.query(
                "SELECT * FROM materiais_enviados ORDER BY data_envio DESC",
                mapper
        );
    }

    public List<MaterialEnviado> findAll(int limit, int offset) {
        return jdbc.query(
                "SELECT * FROM materiais_enviados ORDER BY data_envio DESC LIMIT ? OFFSET ?",
                mapper,
                limit,
                offset
        );
    }

    public boolean delete(int id) {
        return jdbc.update("DELETE FROM materiais_enviados WHERE id=?", id) > 0;
    }

    public int countPendentes() {
        Integer total = jdbc.queryForObject(
                "SELECT COUNT(*) FROM materiais_enviados WHERE status='pendente'",
                Integer.class
        );
        return total != null ? total : 0;
    }

    public List<MaterialEnviado> findAllPendentes() {
        return listarPorStatus("pendente");
    }

    public List<MaterialEnviado> listarPorStatus(String status) {
        return jdbc.query(
                "SELECT * FROM materiais_enviados WHERE status=? ORDER BY data_envio DESC",
                mapper,
                status
        );
    }

    public boolean aprovar(int id, int pontos, String comentario) {
        String sql = """
            UPDATE materiais_enviados
            SET status='aprovado',
                pontos_gerados=?,
                comentario_avaliacao=?,
                data_avaliacao=?
            WHERE id=? AND status='pendente'
        """;

        return jdbc.update(sql,
                pontos,
                comentario,
                LocalDateTime.now(),
                id
        ) > 0;
    }

    public boolean recusar(int id, String comentario) {
        String sql = """
            UPDATE materiais_enviados
            SET status='recusado',
                comentario_avaliacao=?,
                data_avaliacao=?
            WHERE id=? AND status='pendente'
        """;

        return jdbc.update(sql,
                comentario,
                LocalDateTime.now(),
                id
        ) > 0;
    }
}