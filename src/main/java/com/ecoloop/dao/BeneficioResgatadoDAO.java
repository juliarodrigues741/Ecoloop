package com.ecoloop.dao;

import com.ecoloop.model.Beneficio;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeneficioResgatadoDAO {

    private final JdbcTemplate db;

    public BeneficioResgatadoDAO(JdbcTemplate db) {
        this.db = db;
    }

    public List<Integer> buscarIdsResgatados(Integer usuarioId) {
        String sql = "SELECT beneficio_id FROM beneficios_resgatados WHERE usuario_id = ?";
        return db.query(sql, (rs, rowNum) -> rs.getInt("beneficio_id"), usuarioId);
    }

    public List<Beneficio> buscarResgatados(Integer usuarioId) {
        String sql = """
            SELECT b.* FROM beneficios b
            JOIN beneficios_resgatados r ON b.id = r.beneficio_id
            WHERE r.usuario_id = ?
            ORDER BY r.data_resgate DESC
        """;
        return db.query(sql, new BeanPropertyRowMapper<>(Beneficio.class), usuarioId);
    }

    public void salvarResgate(Integer usuarioId, Integer beneficioId) {
        String sql = "INSERT INTO beneficios_resgatados (usuario_id, beneficio_id) VALUES (?, ?)";
        db.update(sql, usuarioId, beneficioId);
    }
}
