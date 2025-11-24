package com.ecoloop.controller;

import com.ecoloop.dao.ConquistaDAO;
import com.ecoloop.model.Conquista;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/conquistas")
public class AdminConquistaController {

    private final ConquistaDAO conquistaDAO;

    public AdminConquistaController(ConquistaDAO conquistaDAO) {
        this.conquistaDAO = conquistaDAO;
    }

    private boolean isAdmin(HttpSession session) {
        var u = (com.ecoloop.model.Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        List<Conquista> lista = conquistaDAO.findAll();
        model.addAttribute("conquistas", lista);
        return "admin/conquistas";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("conquista", new Conquista());
        return "admin/conquista-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Conquista c, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        if (c.getId() == null) conquistaDAO.create(c); else conquistaDAO.update(c);
        return "redirect:/admin/conquistas";
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        conquistaDAO.delete(id);
        return "redirect:/admin/conquistas";
    }
}
