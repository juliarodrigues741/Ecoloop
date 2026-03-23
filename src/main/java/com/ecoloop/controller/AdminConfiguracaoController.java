package com.ecoloop.controller;

import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/config")
public class AdminConfiguracaoController {

    @GetMapping
    public String config(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (usuario.getEmail() == null || !usuario.getEmail().contains("@adm")) {
            return "redirect:/configuracoes";
        }
        model.addAttribute("usuario", usuario);
        return "admin/config";
    }
}
