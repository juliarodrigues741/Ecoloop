package com.ecoloop.controller;

import com.ecoloop.dao.ConquistaDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Conquista;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioConquistaController {

    private final UsuarioDAO usuarioDAO;
    private final ConquistaDAO conquistaDAO;
    private final UsuarioConquistaDAO usuarioConquistaDAO;

    public AdminUsuarioConquistaController(
            UsuarioDAO usuarioDAO,
            ConquistaDAO conquistaDAO,
            UsuarioConquistaDAO usuarioConquistaDAO
    ) {
        this.usuarioDAO = usuarioDAO;
        this.conquistaDAO = conquistaDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
    }

    private boolean isAdmin(HttpSession session) {
        var u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    // === TELA PARA GERENCIAR CONQUISTAS DO USUÁRIO ===
    @GetMapping("/{id}/conquistas")
    public String gerenciarConquistas(
            @PathVariable int id,
            Model model,
            HttpSession session
    ) {
        if (!isAdmin(session)) return "redirect:/login";

        Usuario usuario = usuarioDAO.findById(id);
        List<Conquista> todas = conquistaDAO.findAll();
        List<Integer> marcadas = usuarioConquistaDAO.findConquistaIdsByUsuario(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("todas", todas);
        model.addAttribute("marcadas", marcadas);

        return "admin/usuarios/conquistas"; // ← sem espaço
    }

    @PostMapping("/{id}/conquistas/salvar")
    public String salvar(
            @PathVariable int id,
            @RequestParam(required = false) List<Integer> conquistas,
            HttpSession session
    ) {
        if (!isAdmin(session)) return "redirect:/login";

        usuarioConquistaDAO.deleteAllByUsuario(id);

        if (conquistas != null) {
            for (Integer conquistaId : conquistas) {
                usuarioConquistaDAO.addConquista(id, conquistaId);
            }
        }

        return "redirect:/admin/usuarios/" + id + "/conquistas"; // ← correto
    }

}
