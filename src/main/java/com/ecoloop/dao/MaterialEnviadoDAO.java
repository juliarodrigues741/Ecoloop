package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.MaterialEnviadoDAOInterface;
import com.ecoloop.model.MaterialEnviado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MaterialEnviadoDAO implements MaterialEnviadoDAOInterface {

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
        m.setDataEnvio(rs.getTimestamp("data_envio") != null ? rs.getTimestamp("data_envio").toLocalDateTime() : null);
        m.setPontosGerados(rs.getInt("pontos_gerados"));
        m.setStatus(rs.getString("status"));
        m.setDataAvaliacao(rs.getTimestamp("data_avaliacao") != null ? rs.getTimestamp("data_avaliacao").toLocalDateTime() : null);
        m.setComentarioAvaliacao(rs.getString("comentario_avaliacao"));
        m.setPesoKg(rs.getObject("peso_kg", Double.class) != null ? rs.getDouble("peso_kg") : 0.0);
        m.setTipoMaterial(rs.getString("tipo_material"));
        return m;
    };

    @Override
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

    @Override
    public MaterialEnviado findById(int id) {
        List<MaterialEnviado> list = jdbc.query("SELECT * FROM materiais_enviados WHERE id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<MaterialEnviado> findByUsuarioId(int usuarioId) {
        return jdbc.query("SELECT * FROM materiais_enviados WHERE usuario_id=? ORDER BY data_envio DESC", mapper, usuarioId);
    }

    @Override
    public List<MaterialEnviado> findAllPendentes() {
        return jdbc.query("SELECT * FROM materiais_enviados WHERE status='pendente' ORDER BY data_envio ASC", mapper);
    }

    @Override
    public int countPendentes() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM materiais_enviados WHERE status='pendente'", Integer.class);
    }

    @Override
    public Double sumKgByUsuario(int usuarioId) {
        Double total = jdbc.queryForObject(
                "SELECT COALESCE(SUM(peso_kg),0) FROM materiais_enviados WHERE usuario_id=? AND status='aprovado'",
                Double.class, usuarioId
        );
        return total != null ? total : 0.0;
    }

    @Override
    public boolean aprovar(int id, int pontos, String comentario, Double pesoKg) {
        return jdbc.update("""
            UPDATE materiais_enviados
            SET status='aprovado', pontos_gerados=?, comentario_avaliacao=?, data_avaliacao=?, peso_kg=?
            WHERE id=?
        """, pontos, comentario, LocalDateTime.now(), pesoKg, id) > 0;
    }

    @Override
    public boolean recusar(int id, String comentario) {
        return jdbc.update("""
            UPDATE materiais_enviados
            SET status='recusado', comentario_avaliacao=?, data_avaliacao=?, peso_kg=0
            WHERE id=?
        """, comentario, LocalDateTime.now(), id) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbc.update("DELETE FROM materiais_enviados WHERE id=?", id) > 0;
    }
    
    public List<MaterialEnviado> findAllByUsuario(Integer usuarioId) {
        String sql = """
            SELECT *
            FROM materiais_enviados
            WHERE usuario_id=?
            ORDER BY data_envio DESC
        """;

        return jdbc.query(sql, mapper, usuarioId);
    }

}
