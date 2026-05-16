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
        if (usuario == null) return "redirect:/login";

        ConfiguracoesUsuario config = configDAO.findByUsuarioId(usuario.getId());

        if (config == null) {
            config = new ConfiguracoesUsuario();
            config.setUsuarioId(usuario.getId());
            config.setIdioma("pt-BR");
            config.setTemaEscuro(false);
            config.setNotificacaoEmail(true);

            Integer idGerado = configDAO.create(config);
            config.setId(idGerado);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("config", config);

        return "configuracoes";
    }

    @PostMapping("/configuracoes")
    public String salvar(
            @RequestParam(value = "notificacaoEmail", required = false) Boolean notificacaoEmail,
            HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        ConfiguracoesUsuario c = configDAO.findByUsuarioId(usuario.getId());

        if (c == null) {
            c = new ConfiguracoesUsuario();
            c.setUsuarioId(usuario.getId());
            c.setIdioma("pt-BR");
            c.setTemaEscuro(false);
        }

        c.setNotificacaoEmail(Boolean.TRUE.equals(notificacaoEmail));
        configDAO.saveOrUpdate(c);

        return "redirect:/configuracoes?sucesso=true";
    }
}