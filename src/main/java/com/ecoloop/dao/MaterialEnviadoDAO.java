package com.ecoloop.dao;

import com.ecoloop.model.MaterialEnviado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaterialEnviadoDAO {

    public Integer create(MaterialEnviado m) {
        String sql = "INSERT INTO materiais_enviados (usuario_id, descricao, tipo_arquivo, caminho_arquivo, pontos_gerados, status, comentario_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getUsuarioId());
            ps.setString(2, m.getDescricao());
            ps.setString(3, m.getTipoArquivo());
            ps.setString(4, m.getCaminhoArquivo());
            ps.setInt(5, m.getPontosGerados() == null ? 0 : m.getPontosGerados());
            ps.setString(6, m.getStatus() == null ? "pendente" : m.getStatus());
            ps.setString(7, m.getComentarioAvaliacao());

            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar material enviado", e);
        }
    }

    public boolean update(MaterialEnviado m) {
        String sql = "UPDATE materiais_enviados SET descricao=?, tipo_arquivo=?, caminho_arquivo=?, pontos_gerados=?, status=?, data_avaliacao=?, comentario_avaliacao=? WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getDescricao());
            ps.setString(2, m.getTipoArquivo());
            ps.setString(3, m.getCaminhoArquivo());
            if (m.getPontosGerados() == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, m.getPontosGerados());
            ps.setString(5, m.getStatus());
            if (m.getDataAvaliacao() == null) ps.setNull(6, Types.TIMESTAMP); else ps.setTimestamp(6, Timestamp.valueOf(m.getDataAvaliacao()));
            ps.setString(7, m.getComentarioAvaliacao());
            ps.setInt(8, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar material", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM materiais_enviados WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar material", e);
        }
    }

    public MaterialEnviado findById(int id) {
        String sql = "SELECT * FROM materiais_enviados WHERE id=?";
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar material por id", e);
        }
    }

    public List<MaterialEnviado> findByUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM materiais_enviados WHERE usuario_id=? ORDER BY data_envio DESC";
        List<MaterialEnviado> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar materiais por usuário", e);
        }
    }

    public List<MaterialEnviado> findAll(int limit, int offset) {
        String sql = "SELECT * FROM materiais_enviados ORDER BY data_envio DESC LIMIT ? OFFSET ?";
        List<MaterialEnviado> lista = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar materiais", e);
        }
    }

    private MaterialEnviado mapRow(ResultSet rs) throws SQLException {
        MaterialEnviado m = new MaterialEnviado();
        m.setId(rs.getInt("id"));
        m.setUsuarioId(rs.getInt("usuario_id"));
        m.setDescricao(rs.getString("descricao"));
        m.setTipoArquivo(rs.getString("tipo_arquivo"));
        m.setCaminhoArquivo(rs.getString("caminho_arquivo"));
        Timestamp t = rs.getTimestamp("data_envio");
        if (t != null) m.setDataEnvio(t.toLocalDateTime());
        m.setPontosGerados(rs.getInt("pontos_gerados"));
        m.setStatus(rs.getString("status"));
        Timestamp ta = rs.getTimestamp("data_avaliacao");
        if (ta != null) m.setDataAvaliacao(ta.toLocalDateTime());
        m.setComentarioAvaliacao(rs.getString("comentario_avaliacao"));
        return m;
    }
}
