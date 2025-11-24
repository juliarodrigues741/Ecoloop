package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final UsuarioDAO usuarioDAO;
    private final MaterialEnviadoDAO materialDAO;

    public AdminController(UsuarioDAO usuarioDAO, MaterialEnviadoDAO materialDAO) {
        this.usuarioDAO = usuarioDAO;
        this.materialDAO = materialDAO;
    }

    @GetMapping("/admin")
    public String adminHome(HttpSession session, Model model) {
        var user = (com.ecoloop.model.Usuario) session.getAttribute("usuario");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("totalUsuarios", usuarioDAO.countAll()); // implement in UsuarioDAO
        model.addAttribute("pendentes", materialDAO.countPendentes()); // implement in MaterialEnviadoDAO
        model.addAttribute("listaPendentes", materialDAO.findAllPendentes()); // implement in MaterialEnviadoDAO
        return "admin/index";
    }
}
