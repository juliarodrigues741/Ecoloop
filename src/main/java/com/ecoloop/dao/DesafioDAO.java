package com.ecoloop.dao;

import com.ecoloop.dao.interfaces.DesafioDAOInterface;
import com.ecoloop.model.Desafio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DesafioDAO implements DesafioDAOInterface {

    private final JdbcTemplate jdbc;

    public DesafioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Mapper pode ser privado
    private final RowMapper<Desafio> mapper = (rs, rowNum) -> {
        Desafio d = new Desafio();
        d.setId(rs.getInt("id"));
        d.setTitulo(rs.getString("titulo"));
        d.setDescricao(rs.getString("descricao"));
        d.setPontosRecompensa(rs.getInt("pontos_recompensa"));
        d.setNivelRequerido(rs.getString("nivel_requerido"));
        d.setImagemUrl(rs.getString("imagem_url"));
        d.setMetaKg(rs.getDouble("metaKg"));
        d.setTipoMaterial(rs.getString("tipo_material"));
        return d;
    };

    // CREATE
    @Override
    public void adicionarDesafio(Desafio desafio) {
        final String sql = """
            INSERT INTO desafios 
            (titulo, descricao, pontos_recompensa, nivel_requerido, metaKg, tipo_material, imagem_url)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                desafio.getTitulo(),
                desafio.getDescricao(),
                desafio.getPontosRecompensa(),
                desafio.getNivelRequerido(),
                desafio.getMetaKg(),
                desafio.getTipoMaterial(),
                desafio.getImagemUrl()
        );
    }

    // ======================
    // READ
    // ======================
    @Override
    public List<Desafio> listarDesafios() {
        return jdbc.query("SELECT * FROM desafios", mapper);
    }

    @Override
    public Desafio buscarPorId(int id) {
        List<Desafio> list = jdbc.query("SELECT * FROM desafios WHERE id = ?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    // UPDATE
    @Override
    public void atualizarDesafio(Desafio desafio) {
        final String sql = """
            UPDATE desafios 
            SET titulo = ?, descricao = ?, pontos_recompensa = ?, nivel_requerido = ?, 
                metaKg = ?, tipo_material = ?, imagem_url = ?
            WHERE id = ?
        """;

        jdbc.update(sql,
                desafio.getTitulo(),
                desafio.getDescricao(),
                desafio.getPontosRecompensa(),
                desafio.getNivelRequerido(),
                desafio.getMetaKg(),
                desafio.getTipoMaterial(),
                desafio.getImagemUrl(),
                desafio.getId()
        );
    }

    // DELETE
    @Override
    public void removerDesafio(int id) {
        jdbc.update("DELETE FROM desafios WHERE id = ?", id);
    }
}
