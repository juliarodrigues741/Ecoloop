package com.ecoloop.controller;

import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminConfiguracaoController {

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping("/admin/config")
    public String configAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) return "redirect:/login";
        if (!isAdmin(session)) return "redirect:/dashboard";

        model.addAttribute("usuario", usuario);
        return "admin/config";
    }
}