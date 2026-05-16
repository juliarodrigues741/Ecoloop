package com.ecoloop.dao;

import com.ecoloop.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class UsuarioDAO {

    private final JdbcTemplate jdbc;

    public UsuarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Usuario> mapper = (rs, n) -> {
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

        u.setRole(rs.getString("role"));
        u.setTelefone(rs.getString("telefone"));
        u.setCpf(rs.getString("cpf"));
        return u;
    };

    // ADICIONADO: calcula o nível com base nos pontos totais
    public static String calcularNivel(int pontos) {
        if (pontos >= 5000) return "Diamante";
        if (pontos >= 2000) return "Platina";
        if (pontos >= 1000) return "Ouro";
        if (pontos >= 500)  return "Prata";
        return "Bronze";
    }

    public Usuario findByEmail(String email) {
        List<Usuario> list = jdbc.query(
                "SELECT * FROM usuarios WHERE email=?", mapper, email);
        return list.isEmpty() ? null : list.get(0);
    }

    public Usuario findById(Integer id) {
        List<Usuario> list = jdbc.query(
                "SELECT * FROM usuarios WHERE id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Usuario> findAll() {
        return jdbc.query("SELECT * FROM usuarios ORDER BY id DESC", mapper);
    }

    public List<Usuario> findAllSemAdmin() {
        return jdbc.query(
                "SELECT * FROM usuarios WHERE UPPER(role) <> 'ADMIN' ORDER BY id DESC",
                mapper);
    }

    public int countAll() {
        Integer total = jdbc.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class);
        return total != null ? total : 0;
    }

    public int countAllSemAdmin() {
        Integer total = jdbc.queryForObject(
                "SELECT COUNT(*) FROM usuarios WHERE UPPER(role) <> 'ADMIN'", Integer.class);
        return total != null ? total : 0;
    }

    public List<Usuario> findRankingSemAdmin() {
        return jdbc.query("""
                SELECT * FROM usuarios
                WHERE UPPER(role) <> 'ADMIN'
                ORDER BY pontos DESC, nome ASC
                """, mapper);
    }

    public boolean create(Usuario u) {
        // Garante que novo usuário já entra com nível correto
        u.setNivel(calcularNivel(u.getPontos() != null ? u.getPontos() : 0));

        String sql = """
            INSERT INTO usuarios
            (nome, email, senha_hash, foto_perfil, role, nivel, pontos, telefone, cpf)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return jdbc.update(sql,
                u.getNome(), u.getEmail(), u.getSenhaHash(), u.getFotoPerfil(),
                u.getRole(), u.getNivel(), u.getPontos(), u.getTelefone(), u.getCpf()
        ) > 0;
    }

    public boolean update(Usuario u) {
        String sql = """
            UPDATE usuarios
            SET nome=?, email=?, senha_hash=?, foto_perfil=?, role=?, nivel=?, pontos=?, telefone=?, cpf=?
            WHERE id=?
        """;
        return jdbc.update(sql,
                u.getNome(), u.getEmail(), u.getSenhaHash(), u.getFotoPerfil(),
                u.getRole(), u.getNivel(), u.getPontos(), u.getTelefone(), u.getCpf(),
                u.getId()
        ) > 0;
    }

    public boolean delete(Integer id) {
        return jdbc.update("DELETE FROM usuarios WHERE id=?", id) > 0;
    }

    // CORRIGIDO: soma pontos e recalcula o nível automaticamente
    public boolean addPoints(int userId, int pontos) {
        jdbc.update("UPDATE usuarios SET pontos = pontos + ? WHERE id=?", pontos, userId);

        Integer totalAtual = jdbc.queryForObject(
                "SELECT pontos FROM usuarios WHERE id=?", Integer.class, userId);

        String novoNivel = calcularNivel(totalAtual != null ? totalAtual : 0);

        return jdbc.update(
                "UPDATE usuarios SET nivel=? WHERE id=?", novoNivel, userId) > 0;
    }

    public boolean removePoints(int userId, int pontos) {
        jdbc.update(
                "UPDATE usuarios SET pontos = GREATEST(pontos - ?, 0) WHERE id=?", pontos, userId);

        Integer totalAtual = jdbc.queryForObject(
                "SELECT pontos FROM usuarios WHERE id=?", Integer.class, userId);

        String novoNivel = calcularNivel(totalAtual != null ? totalAtual : 0);

        return jdbc.update(
                "UPDATE usuarios SET nivel=? WHERE id=?", novoNivel, userId) > 0;
    }
}