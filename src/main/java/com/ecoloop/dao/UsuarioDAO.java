package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.UsuarioDAOInterface;
import com.ecoloop.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioDAO implements UsuarioDAOInterface {

    private final JdbcTemplate jdbc;

    public UsuarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Usuario> mapper = (rs, rowNum) -> {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        u.setNivel(rs.getString("nivel"));
        u.setPontos(rs.getInt("pontos"));
        u.setRole(rs.getString("role"));

        if (rs.getTimestamp("data_cadastro") != null) {
            u.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        }
        return u;
    };

    // =====================
    // LOGIN / BUSCAS
    // =====================

    @Override
    public Usuario findByEmail(String email) {
        List<Usuario> list = jdbc.query(
                "SELECT * FROM usuarios WHERE email=?",
                mapper,
                email
        );
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Usuario findById(Integer id) {
        List<Usuario> list = jdbc.query(
                "SELECT * FROM usuarios WHERE id=?",
                mapper,
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Usuario> findAll() {
        return jdbc.query("SELECT * FROM usuarios ORDER BY id DESC", mapper);
    }

    @Override
    public int countAll() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class);
    }

    // =====================
    // CREATE / UPDATE / DELETE
    // =====================

    @Override
    public boolean create(Usuario u) {

        if (u.getNivel() == null || u.getNivel().isBlank()) u.setNivel("Bronze");
        if (u.getPontos() == null) u.setPontos(0);
        if (u.getRole() == null || u.getRole().isBlank()) u.setRole("USER");

        String sql = """
            INSERT INTO usuarios (nome, email, senha_hash, foto_perfil, role, nivel, pontos)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        return jdbc.update(sql,
                u.getNome(),
                u.getEmail(),
                u.getSenhaHash(),
                u.getFotoPerfil(),
                u.getRole(),
                u.getNivel(),
                u.getPontos()
        ) > 0;
    }

    @Override
    public boolean update(Usuario u) {
        String sql = """
            UPDATE usuarios 
            SET nome=?, email=?, senha_hash=?, foto_perfil=?, role=?, nivel=?, pontos=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                u.getNome(),
                u.getEmail(),
                u.getSenhaHash(),
                u.getFotoPerfil(),
                u.getRole(),
                u.getNivel(),
                u.getPontos(),
                u.getId()
        ) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return jdbc.update("DELETE FROM usuarios WHERE id=?", id) > 0;
    }

    @Override
    public boolean addPoints(int userId, int pontos) {
        return jdbc.update(
                "UPDATE usuarios SET pontos = pontos + ? WHERE id=?",
                pontos, userId
        ) > 0;
    }

    @Override
    public boolean atualizarNivel_requerido(Integer id, String nivel) {
        String sql = "UPDATE usuarios SET nivel=? WHERE id=?";
        return jdbc.update(sql, nivel, id) > 0;
    }
}
