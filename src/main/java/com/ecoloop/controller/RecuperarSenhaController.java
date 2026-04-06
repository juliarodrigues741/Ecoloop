package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecuperarSenhaController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping("/esqueceu-senha")
    public String esqueceuSenhaPage() {
        return "esqueceu-senha";
    }

    @PostMapping("/esqueceu-senha")
    public String recuperarSenha(
            @RequestParam String email,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("erro", "Por favor, informe um e-mail válido");
            return "esqueceu-senha";
        }

        Usuario usuario = usuarioDAO.findByEmail(email.trim());

        if (usuario == null) {
            model.addAttribute("erro", "E-mail não encontrado no sistema");
            return "esqueceu-senha";
        }

        // Mostra a senha atual (projeto acadêmico - senha em texto plano)
        model.addAttribute("senhaEncontrada", usuario.getSenhaHash());
        return "esqueceu-senha";
    }
}
