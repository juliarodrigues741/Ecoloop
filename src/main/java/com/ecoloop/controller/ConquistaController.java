package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.model.Usuario;
import com.ecoloop.model.UsuarioConquista;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ConquistaController {

    private final UsuarioConquistaDAO usuarioConquistaDAO;

    public ConquistaController(UsuarioConquistaDAO usuarioConquistaDAO) {
        this.usuarioConquistaDAO = usuarioConquistaDAO;
    }

    @GetMapping("/conquistas")
    public String conquistas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        List<UsuarioConquista> minhas = usuarioConquistaDAO.findByUsuario(usuario.getId());

        model.addAttribute("conquistas", minhas);
        model.addAttribute("usuario", usuario);

        return "conquistas";
    }
}