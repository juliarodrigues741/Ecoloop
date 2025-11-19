package com.ecoloop.dao;

import com.ecoloop.model.RankingLocal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RankingLocalDAO {

    public Integer create(RankingLocal r) {
        String sql = "INSERT INTO ranking_local (usuario_id, posicao, pontos) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getUsuarioId());
            ps.setInt(2, r.getPosicao() == null ? 0 : r.getPosicao());
            ps.setInt(3, r.getPontos() == null ? 0 : r.getPontos());
            int aff = ps.executeUpdate();
            if (aff == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar ranking local", e);
        }
    }

    public boolean update(RankingLocal r) {
        String sql = "UPDATE ranking_local SET usuario_id=?, posicao=?, pontos=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getUsuarioId());
            ps.setInt(2, r.getPosicao() == null ? 0 : r.getPosicao());
            ps.setInt(3, r.getPontos() == null ? 0 : r.getPontos());
            ps.setInt(4, r.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar ranking local", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM ranking_local WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar ranking local", e);
        }
    }

    public RankingLocal findByUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM ranking_local WHERE usuario_id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RankingLocal r = new RankingLocal();
                    r.setId(rs.getInt("id"));
                    r.setUsuarioId(rs.getInt("usuario_id"));
                    r.setPosicao(rs.getInt("posicao"));
                    r.setPontos(rs.getInt("pontos"));
                    return r;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ranking por usuário", e);
        }
    }

    public List<RankingLocal> topN(int limit) {
        String sql = "SELECT * FROM ranking_local ORDER BY pontos DESC LIMIT ?";
        List<RankingLocal> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RankingLocal r = new RankingLocal();
                    r.setId(rs.getInt("id"));
                    r.setUsuarioId(rs.getInt("usuario_id"));
                    r.setPosicao(rs.getInt("posicao"));
                    r.setPontos(rs.getInt("pontos"));
                    lista.add(r);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ranking", e);
        }
    }
}
