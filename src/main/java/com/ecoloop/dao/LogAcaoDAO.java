package com.ecoloop.dao;

import com.ecoloop.model.LogAcao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogAcaoDAO {

    public Integer create(LogAcao l) {
        String sql = "INSERT INTO logs_acoes (usuario_id, acao, descricao) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (l.getUsuarioId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, l.getUsuarioId());
            ps.setString(2, l.getAcao());
            ps.setString(3, l.getDescricao());
            int aff = ps.executeUpdate();
            if (aff == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar log de ação", e);
        }
    }

    public List<LogAcao> findAll(int limit) {
        String sql = "SELECT * FROM logs_acoes ORDER BY data_acao DESC LIMIT ?";
        List<LogAcao> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LogAcao l = new LogAcao();
                    l.setId(rs.getInt("id"));
                    int u = rs.getInt("usuario_id");
                    if (!rs.wasNull()) l.setUsuarioId(u);
                    l.setAcao(rs.getString("acao"));
                    l.setDescricao(rs.getString("descricao"));
                    Timestamp t = rs.getTimestamp("data_acao");
                    if (t != null) l.setDataAcao(t.toLocalDateTime());
                    lista.add(l);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar logs", e);
        }
    }
}
