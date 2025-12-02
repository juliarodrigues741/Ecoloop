package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.BeneficioResgatadoDAOInterface;
import com.ecoloop.model.BeneficioResgatado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeneficioResgatadoDAO implements BeneficioResgatadoDAOInterface {

    private final JdbcTemplate db;

    public BeneficioResgatadoDAO(JdbcTemplate db) {
        this.db = db;
    }

    @Override
    public List<Integer> buscarIdsResgatados(Integer usuarioId) {
        String sql = "SELECT beneficio_id FROM beneficios_resgatados WHERE usuario_id = ?";
        return db.query(sql, (rs, rowNum) -> rs.getInt("beneficio_id"), usuarioId);
    }

    @Override
    public List<BeneficioResgatado> buscarResgatados(Integer usuarioId) {

        String sql = """
            SELECT r.id,
                   r.usuario_id,
                   r.beneficio_id,
                   r.data_resgate,
                   b.nome,
                   b.categoria,
                   b.descricao,
                   b.pontos_necessarios
            FROM beneficios_resgatados r
            JOIN beneficios b ON b.id = r.beneficio_id
            WHERE r.usuario_id = ?
            ORDER BY r.data_resgate DESC
        """;

        return db.query(sql, (rs, n) -> {

            BeneficioResgatado br = new BeneficioResgatado();

            br.setId(rs.getInt("id"));
            br.setUsuarioId(rs.getInt("usuario_id"));
            br.setBeneficioId(rs.getInt("beneficio_id"));

            if (rs.getTimestamp("data_resgate") != null)
                br.setDataResgate(rs.getTimestamp("data_resgate").toLocalDateTime());

            br.setNome(rs.getString("nome"));
            br.setCategoria(rs.getString("categoria"));
            br.setDescricao(rs.getString("descricao"));
            br.setPontos(rs.getInt("pontos_necessarios"));

            return br;
        }, usuarioId);
    }

    @Override
    public void salvarResgate(Integer usuarioId, Integer beneficioId) {
        String sql = "INSERT INTO beneficios_resgatados (usuario_id, beneficio_id, data_resgate) VALUES (?, ?, NOW())";
        db.update(sql, usuarioId, beneficioId);
    }
}
