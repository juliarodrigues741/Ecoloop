package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioDAO usuarioDAO;

    public AdminUsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        List<Usuario> lista = usuarioDAO.findAll();
        model.addAttribute("usuarios", lista);
        return "admin/usuarios";
    }

    @GetMapping("/novo")
    public String novoForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", new Usuario());
        return "admin/usuario-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Usuario usuario, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        if (usuario.getId() == null) {
            usuarioDAO.create(usuario); // implement
        } else {
            usuarioDAO.update(usuario); // implement
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        usuarioDAO.delete(id); // implement
        return "redirect:/admin/usuarios";
    }
}
