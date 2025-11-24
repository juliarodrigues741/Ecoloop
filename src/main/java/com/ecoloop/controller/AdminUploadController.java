package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.dao.NotificacaoDAO;
import com.ecoloop.model.Notificacao;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/uploads")
public class AdminUploadController {

    private final MaterialEnviadoDAO materialDAO;
    private final UsuarioDAO usuarioDAO;
    private final NotificacaoDAO notificacaoDAO;

    public AdminUploadController(MaterialEnviadoDAO materialDAO, UsuarioDAO usuarioDAO, NotificacaoDAO notificacaoDAO) {
        this.materialDAO = materialDAO;
        this.usuarioDAO = usuarioDAO;
        this.notificacaoDAO = notificacaoDAO;
    }

    private boolean isAdmin(HttpSession session) {
        var u = (com.ecoloop.model.Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @PostMapping("/aprovar")
    public String aprovar(
            @RequestParam int id,
            @RequestParam int pontos,
            HttpSession session
    ) {
        if (!isAdmin(session)) return "redirect:/login";

        // 1 — Atualiza o material enviado
        materialDAO.aprovar(id, pontos, "Aprovado pelo administrador");

        // 2 — Recupera material e soma pontos ao usuário
        var m = materialDAO.findById(id);
        usuarioDAO.addPoints(m.getUsuarioId(), pontos);

        // 3 — Cria notificação
        Notificacao n = new Notificacao();
        n.setUsuarioId(m.getUsuarioId());
        n.setMensagem("Seu envio foi aprovado e você recebeu " + pontos + " pontos!");
        n.setTipo("sistema");
        n.setLida(false);
        n.setDataEnvio(LocalDateTime.now());

        notificacaoDAO.create(n);

        return "redirect:/admin";
    }

    @PostMapping("/reprovar")
    public String reprovar(
            @RequestParam int id,
            @RequestParam String comentario,
            HttpSession session
    ) {
        if (!isAdmin(session)) return "redirect:/login";

        // 1 — Atualiza o material para "recusado"
        materialDAO.recusar(id, comentario);

        // 2 — Recupera material
        var m = materialDAO.findById(id);

        // 3 — Cria notificação
        Notificacao n = new Notificacao();
        n.setUsuarioId(m.getUsuarioId());
        n.setMensagem("Seu envio foi recusado: " + comentario);
        n.setTipo("sistema");
        n.setLida(false);
        n.setDataEnvio(LocalDateTime.now());

        notificacaoDAO.create(n);

        return "redirect:/admin";
    }
}
