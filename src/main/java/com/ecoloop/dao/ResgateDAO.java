package com.ecoloop.dao;

import com.ecoloop.model.Resgate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResgateDAO {

    public Integer create(Resgate r) {
        String sql = "INSERT INTO resgates (usuario_id, beneficio_id, pontos_gastos) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getUsuarioId());
            ps.setInt(2, r.getBeneficioId());
            ps.setInt(3, r.getPontosGastos());
            int aff = ps.executeUpdate();
            if (aff == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar resgate", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM resgates WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar resgate", e);
        }
    }

    public Resgate findById(int id) {
        String sql = "SELECT * FROM resgates WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Resgate r = new Resgate();
                    r.setId(rs.getInt("id"));
                    r.setUsuarioId(rs.getInt("usuario_id"));
                    r.setBeneficioId(rs.getInt("beneficio_id"));
                    r.setPontosGastos(rs.getInt("pontos_gastos"));
                    Timestamp t = rs.getTimestamp("data_resgate");
                    if (t != null) r.setDataResgate(t.toLocalDateTime());
                    return r;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar resgate", e);
        }
    }

    public List<Resgate> findByUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM resgates WHERE usuario_id=? ORDER BY data_resgate DESC";
        List<Resgate> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Resgate r = new Resgate();
                    r.setId(rs.getInt("id"));
                    r.setUsuarioId(rs.getInt("usuario_id"));
                    r.setBeneficioId(rs.getInt("beneficio_id"));
                    r.setPontosGastos(rs.getInt("pontos_gastos"));
                    Timestamp t = rs.getTimestamp("data_resgate");
                    if (t != null) r.setDataResgate(t.toLocalDateTime());
                    lista.add(r);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar resgates", e);
        }
    }
}
