package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.model.Beneficio;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/beneficios")
public class AdminBeneficioController {

    private final BeneficioDAO beneficioDAO;

    public AdminBeneficioController(BeneficioDAO beneficioDAO) {
        this.beneficioDAO = beneficioDAO;
    }

    private boolean isAdmin(HttpSession session) {
        var u = (com.ecoloop.model.Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping("")
    public String listar(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("lista", beneficioDAO.findAll());
        return "admin/beneficios/index";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("beneficio", new Beneficio());
        return "admin/beneficios/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Beneficio b, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        if (b.getId() == null)
            beneficioDAO.create(b);
        else
            beneficioDAO.update(b);

        return "redirect:/admin/beneficios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("beneficio", beneficioDAO.findById(id));
        return "admin/beneficios/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        beneficioDAO.delete(id);
        return "redirect:/admin/beneficios";
    }
}
