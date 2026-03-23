package com.ecoloop.dao;

import com.ecoloop.model.Beneficio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneficioDAO {

    public Integer create(Beneficio b) {
        String sql = "INSERT INTO beneficios (nome, descricao, categoria, pontos_necessarios, imagem_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getNome());
            ps.setString(2, b.getDescricao());
            ps.setString(3, b.getCategoria());
            ps.setInt(4, b.getPontosNecessarios() == null ? 0 : b.getPontosNecessarios());
            ps.setString(5, b.getImagemUrl());
            int aff = ps.executeUpdate();
            if (aff == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar benefício", e);
        }
    }

    public boolean update(Beneficio b) {
        String sql = "UPDATE beneficios SET nome=?, descricao=?, categoria=?, pontos_necessarios=?, imagem_url=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getNome());
            ps.setString(2, b.getDescricao());
            ps.setString(3, b.getCategoria());
            ps.setInt(4, b.getPontosNecessarios() == null ? 0 : b.getPontosNecessarios());
            ps.setString(5, b.getImagemUrl());
            ps.setInt(6, b.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar benefício", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM beneficios WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar benefício", e);
        }
    }

    public Beneficio findById(int id) {
        String sql = "SELECT * FROM beneficios WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Beneficio b = new Beneficio();
                    b.setId(rs.getInt("id"));
                    b.setNome(rs.getString("nome"));
                    b.setDescricao(rs.getString("descricao"));
                    b.setCategoria(rs.getString("categoria"));
                    b.setPontosNecessarios(rs.getInt("pontos_necessarios"));
                    b.setImagemUrl(rs.getString("imagem_url"));
                    return b;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar benefício por id", e);
        }
    }

    public List<Beneficio> findAll() {
        String sql = "SELECT * FROM beneficios ORDER BY pontos_necessarios ASC";
        List<Beneficio> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Beneficio b = new Beneficio();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                b.setDescricao(rs.getString("descricao"));
                b.setCategoria(rs.getString("categoria"));
                b.setPontosNecessarios(rs.getInt("pontos_necessarios"));
                b.setImagemUrl(rs.getString("imagem_url"));
                lista.add(b);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar benefícios", e);
        }
    }
}
