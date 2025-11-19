package com.ecoloop.dao;

import com.ecoloop.model.MaterialEnviado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UploadGerenciamentoDAO {

    private final MaterialEnviadoDAO materialDAO = new MaterialEnviadoDAO();

    public List<MaterialEnviado> listarPorStatus(String status) {
        String sql = "SELECT * FROM materiais_enviados WHERE status=? ORDER BY data_envio DESC";
        List<MaterialEnviado> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(materialDAO.findById(rs.getInt("id")));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar uploads por status", e);
        }
    }

    public boolean aprovar(int id, int pontosGerados, String comentario) {
        String sql = "UPDATE materiais_enviados SET status='aprovado', pontos_gerados=?, data_avaliacao=?, comentario_avaliacao=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pontosGerados);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, comentario);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao aprovar upload", e);
        }
    }

    public boolean recusar(int id, String comentario) {
        String sql = "UPDATE materiais_enviados SET status='recusado', data_avaliacao=?, comentario_avaliacao=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, comentario);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao recusar upload", e);
        }
    }
}
