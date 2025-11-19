package com.ecoloop.dao;

import com.ecoloop.model.UsuarioConquista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioConquistaDAO {

    public boolean addConquista(Integer usuarioId, Integer conquistaId) {
        String sql = "INSERT INTO usuarios_conquistas (usuario_id, conquista_id) VALUES (?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, conquistaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar conquista ao usuário", e);
        }
    }

    public boolean removeConquista(Integer usuarioId, Integer conquistaId) {
        String sql = "DELETE FROM usuarios_conquistas WHERE usuario_id=? AND conquista_id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, conquistaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover conquista do usuário", e);
        }
    }

    public List<UsuarioConquista> findByUsuario(int usuarioId) {
        String sql = "SELECT * FROM usuarios_conquistas WHERE usuario_id=? ORDER BY data_conquista DESC";
        List<UsuarioConquista> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UsuarioConquista uc = new UsuarioConquista();
                    uc.setUsuarioId(rs.getInt("usuario_id"));
                    uc.setConquistaId(rs.getInt("conquista_id"));
                    Timestamp t = rs.getTimestamp("data_conquista");
                    if (t != null) uc.setDataConquista(t.toLocalDateTime());
                    lista.add(uc);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar conquistas do usuário", e);
        }
    }
}

