package com.ecoloop.controller;

import com.ecoloop.service.RecuperacaoSenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecuperacaoSenhaController {

    @Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;

    // Exibe a página com o formulário para informar o e-mail
    @GetMapping("/esqueceu-senha")
    public String paginaEsqueceuSenha() {
        return "esqueceu-senha";
    }

    // Exibe a página para definir a nova senha (recebe o token pela URL)
    @GetMapping("/redefinir-senha")
    public String paginaRedefinir(@RequestParam(required = false) String token, Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("erro", "Token não informado.");
            return "redefinir-senha";
        }

        String erro = recuperacaoSenhaService.validarToken(token);

        if (erro != null) {
            model.addAttribute("erro", erro);
            return "redefinir-senha";
        }

        model.addAttribute("token", token);
        return "redefinir-senha";
    }
}
