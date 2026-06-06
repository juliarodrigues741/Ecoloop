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

        int pontos = usuarioAtualizado.getPontos() == null ? 0 : usuarioAtualizado.getPontos();

        String proximoNivel;
        int pontosNecessarios;
        int pontosBase;

        if (pontos >= 5000) {
            proximoNivel = "Nível máximo atingido";
            pontosNecessarios = 5000;
            pontosBase = 5000;
        } else if (pontos >= 2000) {
            proximoNivel = "Diamante";
            pontosNecessarios = 5000;
            pontosBase = 2000;
        } else if (pontos >= 1000) {
            proximoNivel = "Platina";
            pontosNecessarios = 2000;
            pontosBase = 1000;
        } else if (pontos >= 500) {
            proximoNivel = "Ouro";
            pontosNecessarios = 1000;
            pontosBase = 500;
        } else {
            proximoNivel = "Prata";
            pontosNecessarios = 500;
            pontosBase = 0;
        }

        int faltam = Math.max(0, pontosNecessarios - pontos);
        int progresso = pontos >= 5000 ? 100
                : (int) (((pontos - pontosBase) * 100.0) / (pontosNecessarios - pontosBase));
        progresso = Math.min(100, Math.max(0, progresso));

        model.addAttribute("usuario", usuarioAtualizado);
        model.addAttribute("uploads", materialDAO.findByUsuarioId(usuarioAtualizado.getId()));
        model.addAttribute("conquistas", usuarioConquistaDAO.findByUsuario(usuarioAtualizado.getId()));
        model.addAttribute("notificacoes", notificacaoDAO.findByUsuario(usuarioAtualizado.getId()));
        model.addAttribute("proximoNivel", proximoNivel);
        model.addAttribute("faltam", faltam);
        model.addAttribute("progresso", progresso);

        return "dashboard";
    }
}