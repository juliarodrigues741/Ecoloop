package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.model.Beneficio;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminBeneficioController {

    private final BeneficioDAO beneficioDAO;

    public AdminBeneficioController(BeneficioDAO beneficioDAO) {
        this.beneficioDAO = beneficioDAO;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping("/admin/adicionar-recompensa")
    public String listar(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("recompensas", beneficioDAO.findAll());
        return "admin/adicionar-recompensa";
    }

    @GetMapping("/admin/adicionar-recompensa/novo")
    public String novo(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("beneficio", new Beneficio());
        model.addAttribute("modoEdicao", false);
        return "admin/beneficio-form";
    }

    @GetMapping("/admin/beneficios/editar/{id}")
    public String editar(@PathVariable int id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Beneficio beneficio = beneficioDAO.findById(id);
        if (beneficio == null) return "redirect:/admin/adicionar-recompensa";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("beneficio", beneficio);
        model.addAttribute("modoEdicao", true);
        return "admin/beneficio-form";
    }

    @PostMapping("/admin/beneficios/salvar")
    public String salvar(@ModelAttribute Beneficio beneficio, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        if (beneficio.getId() == null) beneficioDAO.create(beneficio);
        else beneficioDAO.update(beneficio);
        return "redirect:/admin/adicionar-recompensa";
    }

    @PostMapping("/admin/beneficios/deletar")
    public String deletar(@RequestParam int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        beneficioDAO.delete(id);
        return "redirect:/admin/adicionar-recompensa";
    }
}