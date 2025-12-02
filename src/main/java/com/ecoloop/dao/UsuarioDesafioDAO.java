package com.ecoloop.dao;

import com.ecoloop.model.Desafio;
import com.ecoloop.model.UsuarioDesafio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UsuarioDesafioDAO {

    private final JdbcTemplate jdbc;

    public UsuarioDesafioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ------------------------------
    // ADICIONAR USUÁRIO AO DESAFIO
    // ------------------------------
    public void adicionarUsuarioDesafio(int usuarioId, int desafioId, LocalDateTime dataConclusao) {
        String sql = """
            INSERT INTO usuario_desafios (usuario_id, desafio_id, data_conclusao, progresso)
            VALUES (?, ?, ?, 0)
        """;
        jdbc.update(sql,
                usuarioId,
                desafioId,
                dataConclusao != null ? Timestamp.valueOf(dataConclusao) : null
        );
    }

    // ------------------------------
    // LISTAR DESAFIOS DO USUÁRIO COM PROGRESSO REAL
    // ------------------------------
    public List<UsuarioDesafio> listarDesafiosPorUsuario(int usuarioId) {

        String sql = """
            SELECT 
                ud.usuario_id,
                ud.desafio_id,
                ud.data_conclusao,
                d.titulo,
                d.descricao,
                d.pontos_recompensa,
                d.metaKg,
                d.imagem_url
            FROM usuario_desafios ud
            JOIN desafios d ON d.id = ud.desafio_id
            WHERE ud.usuario_id = ?
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            UsuarioDesafio ud = new UsuarioDesafio();
            ud.setUsuarioId(rs.getInt("usuario_id"));
            ud.setDesafioId(rs.getInt("desafio_id"));

            Timestamp ts = rs.getTimestamp("data_conclusao");
            ud.setDataConclusao(ts != null ? ts.toLocalDateTime() : null);

            // OBJETO DESAFIO COMPLETO
            Desafio d = new Desafio();
            d.setId(rs.getInt("desafio_id"));
            d.setTitulo(rs.getString("titulo"));
            d.setDescricao(rs.getString("descricao"));
            d.setPontosRecompensa(rs.getInt("pontos_recompensa"));
            d.setMetaKg(rs.getDouble("metaKg"));
            d.setImagemUrl(rs.getString("imagem_url"));

            ud.setDesafio(d);

            // calcular progresso REAL baseado no peso do usuário
            int progresso = calcularProgressoPorDesafio(usuarioId, d.getId());
            ud.setProgresso(progresso);

            return ud;
        }, usuarioId);
    }

    // ------------------------------
    // CALCULAR PROGRESSO BASEADO NO DESAFIO E NO PESO REAL DO USUÁRIO
    // ------------------------------
    public int calcularProgressoPorDesafio(int usuarioId, int desafioId) {

        // 1 — pegar meta e tipo do desafio
        String sqlMeta = "SELECT metaKg, tipo_material FROM desafios WHERE id = ?";
        var row = jdbc.queryForMap(sqlMeta, desafioId);

        Double meta = (Double) row.get("metaKg");
        String tipo = (String) row.get("tipo_material");

        if (meta == null || meta == 0) return 0;
        if (tipo == null || tipo.isBlank()) return 0;

        // 2 — total reciclado APENAS do tipo correto
        String sqlAtual = """
            SELECT IFNULL(SUM(peso_kg), 0)
            FROM materiais_enviados
            WHERE usuario_id = ?
              AND status = 'aprovado'
              AND tipo_material = ?
        """;

        Double atual = jdbc.queryForObject(sqlAtual, Double.class, usuarioId, tipo);
        if (atual == null) atual = 0.0;

        // 3 — porcentagem
        int progresso = (int) ((atual / meta) * 100);
        return Math.min(progresso, 100);
    }


    // ------------------------------
    // REMOVER DESAFIO
    // ------------------------------
    public void removerUsuarioDesafio(int usuarioId, int desafioId) {
        jdbc.update(
                "DELETE FROM usuario_desafios WHERE usuario_id=? AND desafio_id=?",
                usuarioId, desafioId
        );
    }

    // ------------------------------
    // OPCIONAL: atualizar progresso manualmente
    // ------------------------------
    public void atualizarProgresso(int usuarioId, int desafioId, int progresso) {
        jdbc.update(
                "UPDATE usuario_desafios SET progresso=? WHERE usuario_id=? AND desafio_id=?",
                progresso, usuarioId, desafioId
        );
    }
}
