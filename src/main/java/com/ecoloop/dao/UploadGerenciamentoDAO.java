package com.ecoloop.dao;

import com.ecoloop.model.MaterialEnviado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UploadGerenciamentoDAO {

    private final JdbcTemplate jdbc;
    private final MaterialEnviadoDAO materialDAO;

    public UploadGerenciamentoDAO(JdbcTemplate jdbc, MaterialEnviadoDAO materialDAO) {
        this.jdbc = jdbc;
        this.materialDAO = materialDAO;
    }

    public List<MaterialEnviado> listarPorStatus(String status) {
        return jdbc.query(
            "SELECT id FROM materiais_enviados WHERE status=? ORDER BY data_envio DESC",
            (rs, n) -> materialDAO.findById(rs.getInt("id")),
            status
        );
    }

    public boolean aprovar(int id, int pontos, String comentario) {
        String sql = """
            UPDATE materiais_enviados SET
            status='aprovado',
            pontos_gerados=?,
            data_avaliacao=?,
            comentario_avaliacao=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                pontos,
                LocalDateTime.now(),
                comentario,
                id
        ) > 0;
    }

    public boolean recusar(int id, String comentario) {
        String sql = """
            UPDATE materiais_enviados SET
            status='recusado',
            data_avaliacao=?,
            comentario_avaliacao=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                LocalDateTime.now(),
                comentario,
                id
        ) > 0;
    }
}
