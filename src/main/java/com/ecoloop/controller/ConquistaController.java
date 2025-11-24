package com.ecoloop.controller;

import com.ecoloop.dao.ConquistaDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.model.Conquista;
import com.ecoloop.model.Usuario;
import com.ecoloop.model.UsuarioConquista;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ConquistaController {

    private final ConquistaDAO conquistaDAO;
    private final UsuarioConquistaDAO usuarioConquistaDAO;

    public ConquistaController(ConquistaDAO conquistaDAO, UsuarioConquistaDAO usuarioConquistaDAO) {
        this.conquistaDAO = conquistaDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
    }

    @GetMapping("/conquistas")
    public String conquistas(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Todas as conquistas
        List<Conquista> todas = conquistaDAO.findAll();

        // Conquistas do usuário
        List<UsuarioConquista> minhas = usuarioConquistaDAO.findByUsuario(usuario.getId());

        // IDs das conquistas já obtidas
        Set<Integer> idsObtidos = minhas.stream()
                .map(UsuarioConquista::getConquistaId)
                .collect(Collectors.toSet());

        model.addAttribute("conquistas", todas);
        model.addAttribute("obtidas", idsObtidos);

        return "conquistas";
    }
}
