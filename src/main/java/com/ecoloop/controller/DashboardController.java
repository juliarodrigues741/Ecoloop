package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.NotificacaoDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UsuarioDAO usuarioDAO;
    private final MaterialEnviadoDAO materialDAO;
    private final UsuarioConquistaDAO usuarioConquistaDAO;
    private final NotificacaoDAO notificacaoDAO;

    public DashboardController(UsuarioDAO usuarioDAO,
                               MaterialEnviadoDAO materialDAO,
                               UsuarioConquistaDAO usuarioConquistaDAO,
                               NotificacaoDAO notificacaoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.materialDAO = materialDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
        this.notificacaoDAO = notificacaoDAO;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) return "redirect:/login";

        Usuario usuarioAtualizado = usuarioDAO.findByEmail(u.getEmail());
        if (usuarioAtualizado == null) return "redirect:/login";

        session.setAttribute("usuario", usuarioAtualizado);

        model.addAttribute("usuario", usuarioAtualizado);
        model.addAttribute("uploads", materialDAO.findByUsuarioId(usuarioAtualizado.getId()));
        model.addAttribute("conquistas", usuarioConquistaDAO.findByUsuario(usuarioAtualizado.getId()));
        model.addAttribute("notificacoes", notificacaoDAO.findByUsuario(usuarioAtualizado.getId()));

        return "dashboard";
    }
}