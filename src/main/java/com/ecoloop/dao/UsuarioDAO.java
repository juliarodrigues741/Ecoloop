package com.ecoloop.dao;

import com.ecoloop.model.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Integer create(Usuario u) {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, foto_perfil, nivel, pontos) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenhaHash());
            ps.setString(4, u.getFotoPerfil());
            ps.setString(5, u.getNivel());
            ps.setInt(6, u.getPontos() == null ? 0 : u.getPontos());

            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário", e);
        }
    }

    public boolean update(Usuario u) {
        String sql = "UPDATE usuarios SET nome=?, email=?, senha_hash=?, foto_perfil=?, nivel=?, pontos=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenhaHash());
            ps.setString(4, u.getFotoPerfil());
            ps.setString(5, u.getNivel());
            ps.setInt(6, u.getPontos() == null ? 0 : u.getPontos());
            ps.setInt(7, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }

    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id", e);
        }
    }

    public Usuario findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        }
    }

    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuarios ORDER BY pontos DESC, data_cadastro DESC";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        u.setNivel(rs.getString("nivel"));
        u.setPontos(rs.getInt("pontos"));
        Timestamp ts = rs.getTimestamp("data_cadastro");
        if (ts != null) u.setDataCadastro(ts.toLocalDateTime());
        return u;
    }
}
