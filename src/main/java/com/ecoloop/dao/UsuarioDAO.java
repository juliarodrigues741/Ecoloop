package com.ecoloop.dao;

import com.ecoloop.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioDAO {

    private final JdbcTemplate jdbc;

    public UsuarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<Usuario> mapper = (rs, n) -> {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        u.setNivel(rs.getString("nivel"));
        u.setPontos(rs.getInt("pontos"));
        if (rs.getTimestamp("data_cadastro") != null) {
            u.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        }
        u.setRole(rs.getString("role"));
        return u;
    };

    // ======================
    // LOGIN
    // ======================
    public Usuario findByEmail(String email) {
        List<Usuario> list = jdbc.query(
                "SELECT * FROM usuarios WHERE email=?",
                mapper,
                email
        );
        return list.isEmpty() ? null : list.get(0);
    }

    // ======================
    // ADMIN: CRUD
    // ======================

    public Usuario findById(Integer id) {
        List<Usuario> list = jdbc.query(
                "SELECT * FROM usuarios WHERE id=?",
                mapper,
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Usuario> findAll() {
        return jdbc.query(
                "SELECT * FROM usuarios ORDER BY id DESC",
                mapper
        );
    }

    public int countAll() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM usuarios",
                Integer.class
        );
    }

    public boolean create(Usuario u) {
        String sql = """
            INSERT INTO usuarios (nome, email, senha_hash, role, nivel, pontos)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        return jdbc.update(sql,
                u.getNome(),
                u.getEmail(),
                u.getSenhaHash(),
                u.getRole(),
                u.getNivel(),
                u.getPontos()
        ) > 0;
    }

    public boolean update(Usuario u) {
        String sql = """
            UPDATE usuarios SET nome=?, email=?, role=?, nivel=?, pontos=? 
            WHERE id=?
        """;

        return jdbc.update(sql,
                u.getNome(),
                u.getEmail(),
                u.getRole(),
                u.getNivel(),
                u.getPontos(),
                u.getId()
        ) > 0;
    }

    public boolean delete(Integer id) {
        return jdbc.update(
                "DELETE FROM usuarios WHERE id=?",
                id
        ) > 0;
    }
    
    public boolean addPoints(int userId, int pontos) {
        return jdbc.update(
                "UPDATE usuarios SET pontos = pontos + ? WHERE id=?",
                pontos, userId
        ) > 0;
    }

}
