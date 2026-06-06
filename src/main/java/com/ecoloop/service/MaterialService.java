package com.ecoloop.service;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialEnviadoDAO materialDAO;

    public List<String> mensagemStatus(int materialId) {
        List<String> resposta = new ArrayList<>();
        MaterialEnviado material = materialDAO.findById(materialId);
        if (material == null) {
            resposta.add("Material não encontrado.");
            return resposta;
        }
        String status = material.getStatus() == null ? "pendente" : material.getStatus().toLowerCase();
        switch (status) {
            case "pendente" -> {
                resposta.add("📤 Seu arquivo foi enviado com sucesso!");
                resposta.add("Aguarde até que o administrador aprove o seu material.");
            }
            case "aprovado" -> {
                resposta.add("✅ Boa notícia! Seu material foi aprovado pelo administrador.");
                if (material.getPontosGerados() != null && material.getPontosGerados() > 0) {
                    resposta.add("Você ganhou " + material.getPontosGerados() + " pontos!");
                }
            }
            case "recusado" -> {
                resposta.add("❌ Seu material foi recusado pelo administrador.");
                if (material.getComentarioAvaliacao() != null && !material.getComentarioAvaliacao().isBlank()) {
                    resposta.add("Motivo: " + material.getComentarioAvaliacao());
                }
            }
            default -> resposta.add("Status desconhecido: " + status);
        }
        return resposta;
    }

    public List<String> notificacoesUsuario(int usuarioId) {
        List<String> notificacoes = new ArrayList<>();
        for (MaterialEnviado m : materialDAO.findByUsuarioId(usuarioId)) {
            String status = m.getStatus() == null ? "" : m.getStatus().toLowerCase();
            String desc = (m.getDescricao() == null || m.getDescricao().isBlank())
                    ? "Seu material"
                    : "Seu material \"" + m.getDescricao() + "\"";
            if (status.equals("aprovado")) {
                String msg = "✅ " + desc + " foi aprovado pelo administrador.";
                if (m.getPontosGerados() != null && m.getPontosGerados() > 0) {
                    msg += " Você ganhou " + m.getPontosGerados() + " pontos!";
                }
                notificacoes.add(msg);
            } else if (status.equals("recusado")) {
                String msg = "❌ " + desc + " foi recusado pelo administrador.";
                if (m.getComentarioAvaliacao() != null && !m.getComentarioAvaliacao().isBlank()) {
                    msg += " Motivo: " + m.getComentarioAvaliacao();
                }
                notificacoes.add(msg);
            }
        }
        return notificacoes;
    }
}