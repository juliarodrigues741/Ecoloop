package com.ecoloop.controller;

import com.ecoloop.service.RecuperacaoSenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/recuperacao")
public class RecuperacaoSenhaRestController {

    @Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;

    // Solicita o link de redefinição: POST /recuperacao/solicitar
    @PostMapping("/solicitar")
    public Map<String, String> solicitar(@RequestParam String email) {
        Map<String, String> resposta = new HashMap<>();

        String link = recuperacaoSenhaService.solicitarRedefinicao(email);

        if (link == null) {
            resposta.put("status", "erro");
            resposta.put("mensagem", "Nenhuma conta encontrada com esse e-mail.");
        } else {
            resposta.put("status", "sucesso");
            resposta.put("mensagem", "Link de redefinição gerado com sucesso!");
            resposta.put("link", link);
        }

        return resposta;
    }

    // Redefine a senha: POST /recuperacao/redefinir
    @PostMapping("/redefinir")
    public Map<String, String> redefinir(@RequestParam String token,
                                         @RequestParam String novaSenha,
                                         @RequestParam String confirmarSenha) {
        Map<String, String> resposta = new HashMap<>();

        String erro = recuperacaoSenhaService.redefinirSenha(token, novaSenha, confirmarSenha);

        if (erro != null) {
            resposta.put("status", "erro");
            resposta.put("mensagem", erro);
        } else {
            resposta.put("status", "sucesso");
            resposta.put("mensagem", "Senha redefinida com sucesso! Você já pode fazer login.");
        }

        return resposta;
    }
}
