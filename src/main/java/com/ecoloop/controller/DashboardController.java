package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.NotificacaoDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.ConquistaDoUsuario;
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

        // verifica sessão
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) {
            return "redirect:/login";
        }

        // carrega usuário atualizado
        Usuario usuarioAtualizado = usuarioDAO.findByEmail(u.getEmail());
        model.addAttribute("usuario", usuarioAtualizado);

        // uploads
        model.addAttribute("uploads",
                materialDAO.findByUsuarioId(usuarioAtualizado.getId())
        );

        // CONQUISTAS → agora trazendo informações completas + data_conquista
        model.addAttribute("conquistas",
                usuarioConquistaDAO.findConquistasCompletasByUsuario(usuarioAtualizado.getId())
        );

        // notificações
        model.addAttribute("notificacoes",
                notificacaoDAO.findByUsuario(usuarioAtualizado.getId())
        );

        return "dashboard";
    }
}
