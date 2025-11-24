package com.ecoloop.controller;

import com.ecoloop.dao.UploadGerenciamentoDAO;
import com.ecoloop.model.Usuario;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadGerenciamentoController {

    private final UploadGerenciamentoDAO uploadGerenciamentoDAO;

    public UploadGerenciamentoController(UploadGerenciamentoDAO uploadGerenciamentoDAO) {
        this.uploadGerenciamentoDAO = uploadGerenciamentoDAO;
    }

    @GetMapping("/uploads")
    public String uploads(Model model, HttpSession session) {

        Usuario logado = (Usuario) session.getAttribute("usuario");

        if (logado == null) return "redirect:/login";

        if (!"ADMIN".equalsIgnoreCase(logado.getRole())) {
            return "redirect:/dashboard";
        }

        // Envia apenas uploads pendentes para o front
        model.addAttribute("uploads",
                uploadGerenciamentoDAO.listarPorStatus("pendente"));

        return "uploads"; // templates/uploads.html
    }
}
