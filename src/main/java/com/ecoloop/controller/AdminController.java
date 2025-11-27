package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UsuarioDAO usuarioDAO;
    private final MaterialEnviadoDAO materialDAO;

    public AdminController(UsuarioDAO usuarioDAO, MaterialEnviadoDAO materialDAO) {
        this.usuarioDAO = usuarioDAO;
        this.materialDAO = materialDAO;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping("/admin")
    public String adminHome(HttpSession session, Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalUsuarios", usuarioDAO.countAll());
        model.addAttribute("pendentes", materialDAO.countPendentes());
        model.addAttribute("listaPendentes", materialDAO.findAllPendentes());

        // Caminho correto do template:
        return "admin/index";
    }
}
