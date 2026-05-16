package com.ecoloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RankingService {

    @Autowired
    private JdbcTemplate jdbc;

    public List<String> obterTop10() {
        String sql = """
            SELECT nome, nivel, pontos
            FROM usuarios
            WHERE role <> 'ADMIN'
            ORDER BY pontos DESC
            LIMIT 10
        """;

        List<Map<String, Object>> resultados = jdbc.queryForList(sql);
        List<String> ranking = new ArrayList<>();

        int posicao = 1;
        for (Map<String, Object> linha : resultados) {
            String nome = (String) linha.get("nome");
            String nivel = (String) linha.get("nivel");
            Integer pontos = (Integer) linha.get("pontos");

            ranking.add(posicao + "º lugar: " + nome
                    + " | Nível: " + nivel
                    + " | " + pontos + " pontos");
            posicao++;
        }

        return ranking;
    }
}