package com.ecoloop.controller;

import org.jspecify.annotations.Nullable;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping("/")
    public String home() {
        return "index";
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

            if (username.contains("@adm")) {
                return "redirect:/admin/dashboard";
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
    