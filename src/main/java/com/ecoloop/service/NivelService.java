package com.ecoloop.service;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NivelService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    public List<String> calcularProgresso(int usuarioId) {
        List<String> resposta = new ArrayList<>();

        Usuario usuario = usuarioDAO.findById(usuarioId);
        if (usuario == null) {
            resposta.add("Usuário não encontrado.");
            return resposta;
        }

        int pontosAtuais = usuario.getPontos() == null ? 0 : usuario.getPontos();
        String nivelAtual = usuario.getNivel();

        // Define o próximo nível e quantos pontos ele exige
        String proximoNivel;
        int pontosNecessarios;

        if (pontosAtuais >= 5000) {
            proximoNivel = "Nível máximo atingido";
            pontosNecessarios = 5000;
        } else if (pontosAtuais >= 2000) {
            proximoNivel = "Diamante";
            pontosNecessarios = 5000;
        } else if (pontosAtuais >= 1000) {
            proximoNivel = "Platina";
            pontosNecessarios = 2000;
        } else if (pontosAtuais >= 500) {
            proximoNivel = "Ouro";
            pontosNecessarios = 1000;
        } else {
            proximoNivel = "Prata";
            pontosNecessarios = 500;
        }

        // Monta a resposta
        resposta.add("Usuário: " + usuario.getNome());
        resposta.add("Nível atual: " + nivelAtual);
        resposta.add("Pontos atuais: " + pontosAtuais);

        if (proximoNivel.equals("Nível máximo atingido")) {
            resposta.add("Parabéns! Você atingiu o nível máximo Diamante!");
        } else {
            int faltam = pontosNecessarios - pontosAtuais;
            int progresso = (int) ((pontosAtuais * 100.0) / pontosNecessarios);

            resposta.add("Próximo nível: " + proximoNivel);
            resposta.add("Faltam " + faltam + " pontos para subir de nível");
            resposta.add("Progresso: " + progresso + "%");
        }

        return resposta;
    }
}