package com.ecoloop.dao;

import com.ecoloop.model.ConfiguracoesUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracoesUsuarioDAO {

    public Integer create(ConfiguracoesUsuario c) {
        String sql = "INSERT INTO configuracoes_usuario (usuario_id, idioma, tema_escuro, notificacao_email) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getUsuarioId());
            ps.setString(2, c.getIdioma());
            ps.setBoolean(3, c.getTemaEscuro() == null ? false : c.getTemaEscuro());
            ps.setBoolean(4, c.getNotificacaoEmail() == null ? true : c.getNotificacaoEmail());
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar configuração de usuário", e);
        }
    }

    public boolean update(ConfiguracoesUsuario c) {
        String sql = "UPDATE configuracoes_usuario SET idioma=?, tema_escuro=?, notificacao_email=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getIdioma());
            ps.setBoolean(2, c.getTemaEscuro() == null ? false : c.getTemaEscuro());
            ps.setBoolean(3, c.getNotificacaoEmail() == null ? true : c.getNotificacaoEmail());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar configuração de usuário", e);
        }
    }

    public ConfiguracoesUsuario findByUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM configuracoes_usuario WHERE usuario_id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ConfiguracoesUsuario c = new ConfiguracoesUsuario();
                    c.setId(rs.getInt("id"));
                    c.setUsuarioId(rs.getInt("usuario_id"));
                    c.setIdioma(rs.getString("idioma"));
                    c.setTemaEscuro(rs.getBoolean("tema_escuro"));
                    c.setNotificacaoEmail(rs.getBoolean("notificacao_email"));
                    return c;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar configuração por usuário", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM configuracoes_usuario WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar configuração", e);
        }
    }

    public List<ConfiguracoesUsuario> findAll() {
        String sql = "SELECT * FROM configuracoes_usuario";
        List<ConfiguracoesUsuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConfiguracoesUsuario c = new ConfiguracoesUsuario();
                c.setId(rs.getInt("id"));
                c.setUsuarioId(rs.getInt("usuario_id"));
                c.setIdioma(rs.getString("idioma"));
                c.setTemaEscuro(rs.getBoolean("tema_escuro"));
                c.setNotificacaoEmail(rs.getBoolean("notificacao_email"));
                lista.add(c);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar configurações", e);
        }
    }
}

