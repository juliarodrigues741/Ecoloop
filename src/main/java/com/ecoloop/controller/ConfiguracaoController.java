package com.ecoloop.controller;

import com.ecoloop.dao.ConfiguracoesUsuarioDAO;
import com.ecoloop.model.ConfiguracoesUsuario;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConfiguracaoController {

    private final ConfiguracoesUsuarioDAO configDAO;

    public ConfiguracaoController(ConfiguracoesUsuarioDAO configDAO) {
        this.configDAO = configDAO;
    }

    @GetMapping("/configuracoes")
    public String configuracoes(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        ConfiguracoesUsuario config =
                configDAO.findByUsuarioId(usuario.getId());

        model.addAttribute("config", config);

        return "configuracoes";
    }

    @PostMapping("/configuracoes")
    public String salvar(
            @RequestParam String idioma,
            @RequestParam(required = false) boolean temaEscuro,
            @RequestParam(required = false) boolean notificacaoEmail,
            HttpSession session
    ) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        ConfiguracoesUsuario c = configDAO.findByUsuarioId(usuario.getId());
        c.setIdioma(idioma);
        c.setTemaEscuro(temaEscuro);
        c.setNotificacaoEmail(notificacaoEmail);

        configDAO.update(c);

        return "redirect:/configuracoes?sucesso=true";
    }
}
