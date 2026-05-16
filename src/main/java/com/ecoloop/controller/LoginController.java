package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpSession session) {

        Usuario usuario = usuarioDAO.findByEmail(username);

        // Usuário não encontrado OU senha errada → mesma mensagem (boa prática de segurança)
        if (usuario == null
                || usuario.getSenhaHash() == null
                || !usuario.getSenhaHash().equals(password)) {
            return "redirect:/login?erro=true";
        }

        session.setAttribute("usuario", usuario);

        if ("ADMIN".equalsIgnoreCase(usuario.getRole())) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
