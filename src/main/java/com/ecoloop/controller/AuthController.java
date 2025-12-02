package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioDAO usuarioDAO;

    public AuthController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Página inicial
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Página de login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Processar login
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


    // Página de alterar senha com e-mail carregado
 // Mostra a página
    @GetMapping("/recuperar-senha")
    public String recuperarSenhaPage(Model model) {
        model.addAttribute("usuario", null);
        model.addAttribute("erro", false);
        return "recuperar-senha";
    }

    // Processa o formulário
    @PostMapping("/recuperar-senha")
    public String processarRecuperacao(
            @RequestParam String email,
            @RequestParam(required = false) String senhaNova,
            Model model
    ) {
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            // Usuário não encontrado
            model.addAttribute("usuario", null);
            model.addAttribute("erro", "Usuário não cadastrado!");
            model.addAttribute("sucesso", null);
            return "recuperar-senha";
        }

        if (senhaNova != null && !senhaNova.isEmpty()) {
            // Atualiza a senha
            usuario.setSenhaHash(senhaNova);
            usuarioDAO.update(usuario);

            model.addAttribute("usuario", null);
            model.addAttribute("erro", null);
            model.addAttribute("sucesso", "Senha atualizada com sucesso!");
            return "recuperar-senha";
        }

        // Se chegou aqui, apenas mostrar o formulário de nova senha
        model.addAttribute("usuario", usuario);
        model.addAttribute("erro", null);
        model.addAttribute("sucesso", null);
        return "recuperar-senha";
    }


    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/login";
    }
}
