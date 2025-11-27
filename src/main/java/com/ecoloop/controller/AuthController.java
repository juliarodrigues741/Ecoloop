package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioDAO usuarioDAO;

    public AuthController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session
    ) {

        Usuario u = usuarioDAO.findByEmail(username);

        if (u != null && u.getSenhaHash().equals(password)) {

            session.setAttribute("usuario", u);

            if ("ADMIN".equalsIgnoreCase(u.getRole())) {
                return "redirect:/admin";      
            } else {
                return "redirect:/dashboard";  
            }
        }

        return "redirect:/login?erro=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/login";
    }
}
