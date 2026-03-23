package com.ecoloop.dao;

import com.ecoloop.model.Notificacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {

    public Integer create(Notificacao n) {
        String sql = "INSERT INTO notificacoes (usuario_id, mensagem, tipo, lida) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, n.getUsuarioId());
            ps.setString(2, n.getMensagem());
            ps.setString(3, n.getTipo());
            ps.setBoolean(4, n.getLida() == null ? false : n.getLida());
            int aff = ps.executeUpdate();
            if (aff == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar notificação", e);
        }
    }

    public boolean marcarComoLida(int id) {
        String sql = "UPDATE notificacoes SET lida=TRUE WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao marcar notificação como lida", e);
        }
    }

    public List<Notificacao> findByUsuario(int usuarioId) {
        String sql = "SELECT * FROM notificacoes WHERE usuario_id=? ORDER BY data_envio DESC";
        List<Notificacao> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacao n = new Notificacao();
                    n.setId(rs.getInt("id"));
                    n.setUsuarioId(rs.getInt("usuario_id"));
                    n.setMensagem(rs.getString("mensagem"));
                    n.setTipo(rs.getString("tipo"));
                    n.setLida(rs.getBoolean("lida"));
                    Timestamp t = rs.getTimestamp("data_envio");
                    if (t != null) n.setDataEnvio(t.toLocalDateTime());
                    lista.add(n);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notificações", e);
        }
    }
}
