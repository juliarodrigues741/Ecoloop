package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.dao.ConnectionBD;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        // Total de usuários
        List<Usuario> todos = usuarioDAO.findAll();
        model.addAttribute("totalUsuarios", todos.size());

        // Top 5 ranking (já ordenado por pontos DESC no findAll)
        List<Usuario> ranking = todos.size() > 5 ? todos.subList(0, 5) : todos;
        model.addAttribute("ranking", ranking);

        // Total de pontos somados
        int totalPontos = todos.stream().mapToInt(u -> u.getPontos() == null ? 0 : u.getPontos()).sum();
        model.addAttribute("totalPontos", totalPontos);

        // Coletas pendentes
        int coletasPendentes = 0;
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT COUNT(*) FROM materiais_enviados WHERE status = 'pendente'");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) coletasPendentes = rs.getInt(1);
        } catch (Exception e) {
            coletasPendentes = 0;
        }
        model.addAttribute("coletasPendentes", coletasPendentes);

        model.addAttribute("usuario", usuario);
        return "admin/dashboard";
    }
}