package com.ecoloop.dao;

import com.ecoloop.model.Beneficio;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeneficioResgatadoDAO {

    private final JdbcTemplate jdbc;

    public BeneficioResgatadoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Beneficio> beneficioMapper = (rs, rowNum) -> {
        Beneficio b = new Beneficio();
        b.setId(rs.getInt("id"));
        b.setNome(rs.getString("nome"));
        b.setDescricao(rs.getString("descricao"));
        b.setCategoria(rs.getString("categoria"));
        b.setPontosNecessarios(rs.getInt("pontos_necessarios"));
        b.setImagemUrl(rs.getString("imagem_url"));
        return b;
    };

    public List<Integer> buscarIdsResgatados(Integer usuarioId) {
        String sql = """
            SELECT beneficio_id
            FROM beneficios_resgatados
            WHERE usuario_id = ?
            ORDER BY data_resgate DESC
        """;
        return jdbc.query(sql, (rs, rowNum) -> rs.getInt("beneficio_id"), usuarioId);
    }

    public List<Beneficio> buscarResgatados(Integer usuarioId) {
        String sql = """
            SELECT b.*
            FROM beneficios b
            INNER JOIN beneficios_resgatados r ON b.id = r.beneficio_id
            WHERE r.usuario_id = ?
            ORDER BY r.data_resgate DESC
        """;
        return jdbc.query(sql, beneficioMapper, usuarioId);
    }

    public boolean jaResgatado(Integer usuarioId, Integer beneficioId) {
        String sql = """
            SELECT COUNT(*)
            FROM beneficios_resgatados
            WHERE usuario_id = ? AND beneficio_id = ?
        """;
        Integer total = jdbc.queryForObject(sql, Integer.class, usuarioId, beneficioId);
        return total != null && total > 0;
    }

    public boolean salvarResgate(Integer usuarioId, Integer beneficioId) {
        String sql = """
            INSERT INTO beneficios_resgatados (usuario_id, beneficio_id)
            VALUES (?, ?)
        """;
        try {
            return jdbc.update(sql, usuarioId, beneficioId) > 0;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }
}