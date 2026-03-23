package com.ecoloop.controller;

import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfiguracaoController {

    @GetMapping("/configuracoes")
    public String configuracoes(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (usuario.getEmail() != null && usuario.getEmail().contains("@adm")) {
            return "redirect:/admin/config";
        }
        return "configuracoes";
    }
}
