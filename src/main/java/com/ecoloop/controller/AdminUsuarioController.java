package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminUsuarioController {

    private final UsuarioDAO usuarioDAO;

    public AdminUsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping("/admin/adicionar-usuario")
    public String listar(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("usuarios", usuarioDAO.findAllSemAdmin());
        return "admin/adicionar-usuario";
    }

    @GetMapping("/admin/adicionar-usuario/novo")
    public String novo(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("usuarioEdicao", new Usuario());
        model.addAttribute("modoEdicao", false);
        return "admin/usuario-form";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Usuario usuarioEdicao = usuarioDAO.findById(id);
        if (usuarioEdicao == null) return "redirect:/admin/adicionar-usuario";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("usuarioEdicao", usuarioEdicao);
        model.addAttribute("modoEdicao", true);
        return "admin/usuario-form";
    }

    @PostMapping("/admin/usuarios/salvar")
    public String salvar(@ModelAttribute Usuario usuarioEdicao, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        if (usuarioEdicao.getRole() == null || usuarioEdicao.getRole().isBlank()) usuarioEdicao.setRole("USER");
        if (usuarioEdicao.getNivel() == null || usuarioEdicao.getNivel().isBlank()) usuarioEdicao.setNivel("Bronze");
        if (usuarioEdicao.getPontos() == null) usuarioEdicao.setPontos(0);

        if (usuarioEdicao.getId() == null) {
            if (usuarioEdicao.getSenhaHash() == null || usuarioEdicao.getSenhaHash().isBlank()) usuarioEdicao.setSenhaHash("123456");
            usuarioDAO.create(usuarioEdicao);
        } else {
            Usuario atual = usuarioDAO.findById(usuarioEdicao.getId());
            if (atual == null) return "redirect:/admin/adicionar-usuario";
            if (usuarioEdicao.getSenhaHash() == null || usuarioEdicao.getSenhaHash().isBlank()) usuarioEdicao.setSenhaHash(atual.getSenhaHash());
            if (usuarioEdicao.getFotoPerfil() == null || usuarioEdicao.getFotoPerfil().isBlank()) usuarioEdicao.setFotoPerfil(atual.getFotoPerfil());
            usuarioDAO.update(usuarioEdicao);
        }
        return "redirect:/admin/adicionar-usuario";
    }

    @PostMapping("/admin/usuarios/deletar")
    public String deletar(@RequestParam Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        usuarioDAO.delete(id);
        return "redirect:/admin/adicionar-usuario";
    }
}