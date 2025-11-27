package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    private final UsuarioDAO usuarioDAO;

    public PerfilController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {

        // Verifica login
        Usuario logado = (Usuario) session.getAttribute("usuario");
        if (logado == null) {
            return "redirect:/login";
        }

        // Puxa os dados atualizados do banco
        Usuario usuario = usuarioDAO.findByEmail(logado.getEmail());

        model.addAttribute("usuario", usuario);

        return "perfil";
    }
}

