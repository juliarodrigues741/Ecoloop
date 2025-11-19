package com.ecoloop.dao;

import com.ecoloop.model.Conquista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConquistaDAO {

    public Integer create(Conquista c) {
        String sql = "INSERT INTO conquistas (titulo, descricao, pontos_recompensa, nivel_requerido, imagem_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getTitulo());
            ps.setString(2, c.getDescricao());
            ps.setInt(3, c.getPontosRecompensa() == null ? 0 : c.getPontosRecompensa());
            ps.setString(4, c.getNivelRequerido());
            ps.setString(5, c.getImagemUrl());
            int aff = ps.executeUpdate();
            if (aff == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar conquista", e);
        }
    }

    public boolean update(Conquista c) {
        String sql = "UPDATE conquistas SET titulo=?, descricao=?, pontos_recompensa=?, nivel_requerido=?, imagem_url=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getTitulo());
            ps.setString(2, c.getDescricao());
            ps.setInt(3, c.getPontosRecompensa() == null ? 0 : c.getPontosRecompensa());
            ps.setString(4, c.getNivelRequerido());
            ps.setString(5, c.getImagemUrl());
            ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar conquista", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM conquistas WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar conquista", e);
        }
    }

    public Conquista findById(int id) {
        String sql = "SELECT * FROM conquistas WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Conquista c = new Conquista();
                    c.setId(rs.getInt("id"));
                    c.setTitulo(rs.getString("titulo"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setPontosRecompensa(rs.getInt("pontos_recompensa"));
                    c.setNivelRequerido(rs.getString("nivel_requerido"));
                    c.setImagemUrl(rs.getString("imagem_url"));
                    return c;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conquista", e);
        }
    }

    public List<Conquista> findAll() {
        String sql = "SELECT * FROM conquistas ORDER BY pontos_recompensa DESC";
        List<Conquista> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Conquista c = new Conquista();
                c.setId(rs.getInt("id"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescricao(rs.getString("descricao"));
                c.setPontosRecompensa(rs.getInt("pontos_recompensa"));
                c.setNivelRequerido(rs.getString("nivel_requerido"));
                c.setImagemUrl(rs.getString("imagem_url"));
                lista.add(c);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar conquistas", e);
        }
    }
}
