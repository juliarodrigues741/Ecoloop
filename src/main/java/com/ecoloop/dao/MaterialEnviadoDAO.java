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

    private final RowMapper<MaterialEnviado> mapper = (rs, n) -> {
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

        // peso_kg pode ser null → tratar corretamente:
        Double peso = rs.getObject("peso_kg", Double.class);
        m.setPesoKg(peso != null ? peso : 0.0);

        // tipo_material (novo campo)
        m.setTipoMaterial(rs.getString("tipo_material"));

        return m;
    };

    // =====================================
    // CREATE
    // =====================================
    public Integer create(MaterialEnviado m) {

        String sql = """
            INSERT INTO materiais_enviados
            (usuario_id, descricao, tipo_arquivo, caminho_arquivo, tipo_material,
             pontos_gerados, status, comentario_avaliacao, peso_kg, data_envio)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                m.getUsuarioId(),
                m.getDescricao(),
                m.getTipoArquivo(),
                m.getCaminhoArquivo(),
                m.getTipoMaterial(),
                m.getPontosGerados(),
                m.getStatus(),
                m.getComentarioAvaliacao(),
                m.getPesoKg(),
                LocalDateTime.now()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // =====================================
    // FINDERS
    // =====================================
    public MaterialEnviado findById(int id) {
        List<MaterialEnviado> list = jdbc.query(
                "SELECT * FROM materiais_enviados WHERE id=?",
                mapper, id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public List<MaterialEnviado> findByUsuarioId(int usuarioId) {
        return jdbc.query(
                "SELECT * FROM materiais_enviados WHERE usuario_id=? ORDER BY data_envio DESC",
                mapper, usuarioId
        );
    }

    public List<MaterialEnviado> findAllPendentes() {
        return jdbc.query(
                "SELECT * FROM materiais_enviados WHERE status='pendente' ORDER BY data_envio ASC",
                mapper
        );
    }

    public int countPendentes() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM materiais_enviados WHERE status='pendente'",
                Integer.class
        );
    }

    public Double sumKgByUsuario(int usuarioId) {
        Double valor = jdbc.queryForObject(
                "SELECT SUM(peso_kg) FROM materiais_enviados WHERE usuario_id=? AND status='aprovado'",
                Double.class, usuarioId
        );
        return valor != null ? valor : 0.0;
    }

    // =====================================
    // UPDATE STATUS
    // =====================================
    public boolean aprovar(int id, int pontos, String comentario) {
        String sql = """
            UPDATE materiais_enviados
            SET status='aprovado',
                pontos_gerados=?,
                comentario_avaliacao=?,
                data_avaliacao=?,
                peso_kg=peso_kg
            WHERE id=?
        """;

        return jdbc.update(sql, pontos, comentario, LocalDateTime.now(), id) > 0;
    }

    public boolean recusar(int id, String comentario) {
        String sql = """
            UPDATE materiais_enviados
            SET status='recusado',
                comentario_avaliacao=?,
                data_avaliacao=?,
                peso_kg=0
            WHERE id=?
        """;

        return jdbc.update(sql, comentario, LocalDateTime.now(), id) > 0;
    }

    // =====================================
    // DELETE
    // =====================================
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM materiais_enviados WHERE id=?", id) > 0;
    }
}
