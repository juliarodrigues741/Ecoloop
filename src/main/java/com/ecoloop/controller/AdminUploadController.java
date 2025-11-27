package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.dao.NotificacaoDAO;
import com.ecoloop.model.Notificacao;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // NOVO - carrega a tela correta
    @GetMapping("")
    public String listar(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("lista", materialDAO.findAllPendentes());
        return "admin/uploads/index";
    }

    @PostMapping("/aprovar")
    public String aprovar(@RequestParam int id, @RequestParam int pontos, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        materialDAO.aprovar(id, pontos, "Aprovado pelo administrador");
        var m = materialDAO.findById(id);
        usuarioDAO.addPoints(m.getUsuarioId(), pontos);

        Notificacao n = new Notificacao();
        n.setUsuarioId(m.getUsuarioId());
        n.setMensagem("Seu envio foi aprovado e você recebeu " + pontos + " pontos!");
        n.setTipo("sistema");
        n.setLida(false);
        n.setDataEnvio(LocalDateTime.now());
        notificacaoDAO.create(n);

        return "redirect:/admin/uploads";
    }

    @PostMapping("/reprovar")
    public String reprovar(@RequestParam int id, @RequestParam String comentario, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        materialDAO.recusar(id, comentario);
        var m = materialDAO.findById(id);

        Notificacao n = new Notificacao();
        n.setUsuarioId(m.getUsuarioId());
        n.setMensagem("Seu envio foi recusado: " + comentario);
        n.setTipo("sistema");
        n.setLida(false);
        n.setDataEnvio(LocalDateTime.now());
        notificacaoDAO.create(n);

        return "redirect:/admin/uploads";
    }
}
