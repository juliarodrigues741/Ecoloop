package com.ecoloop.dao;

import com.ecoloop.model.Beneficio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeneficioDAO {

    private final JdbcTemplate jdbc;

    public BeneficioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Beneficio> mapper = (rs, rowNum) -> {
        Beneficio b = new Beneficio();
        b.setId(rs.getInt("id"));
        b.setNome(rs.getString("nome"));
        b.setDescricao(rs.getString("descricao"));
        b.setCategoria(rs.getString("categoria"));
        b.setPontosNecessarios(rs.getInt("pontos_necessarios"));
        b.setImagemUrl(rs.getString("imagem_url"));
        return b;
    };

    public Integer create(Beneficio b) {
        String sql = """
            INSERT INTO beneficios (nome, descricao, categoria, pontos_necessarios, imagem_url)
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                b.getNome(),
                b.getDescricao(),
                b.getCategoria(),
                b.getPontosNecessarios(),
                b.getImagemUrl()
        );

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public boolean update(Beneficio b) {
        String sql = """
            UPDATE beneficios
            SET nome=?, descricao=?, categoria=?, pontos_necessarios=?, imagem_url=?
            WHERE id=?
        """;

        return jdbc.update(sql,
                b.getNome(),
                b.getDescricao(),
                b.getCategoria(),
                b.getPontosNecessarios(),
                b.getImagemUrl(),
                b.getId()
        ) > 0;
    }

    public boolean delete(int id) {
        return jdbc.update("DELETE FROM beneficios WHERE id=?", id) > 0;
    }

    public Beneficio findById(int id) {
        List<Beneficio> list = jdbc.query(
                "SELECT * FROM beneficios WHERE id=?",
                mapper,
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Beneficio> findAll() {
        return jdbc.query(
                "SELECT * FROM beneficios ORDER BY pontos_necessarios ASC, nome ASC",
                mapper
        );
    }
}