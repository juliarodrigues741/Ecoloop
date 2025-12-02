package com.ecoloop.controller;

import com.ecoloop.dao.interfaces.ConfiguracoesUsuarioDAOInterface;
import com.ecoloop.dao.interfaces.UsuarioDAOInterface;
import com.ecoloop.model.ConfiguracoesUsuario;
import com.ecoloop.model.Usuario;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConfiguracaoController {

    private final ConfiguracoesUsuarioDAOInterface configDAO;
    private final UsuarioDAOInterface usuarioDAO;

    public ConfiguracaoController(ConfiguracoesUsuarioDAOInterface configDAO,
                                  UsuarioDAOInterface usuarioDAO) {
        this.configDAO = configDAO;
        this.usuarioDAO = usuarioDAO;
    }

    // GET - Página de Configurações
    @GetMapping("/configuracoes")
    public String configuracoes(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        ConfiguracoesUsuario config = configDAO.findByUsuarioId(usuario.getId());

        if (config == null) {
            config = new ConfiguracoesUsuario();
            config.setUsuario(usuario);   // associa ao usuário
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("config", config);

        return "configuracoes";
    }

    // POST - Salvar preferências (idioma, tema, notificações)
    @PostMapping("/configuracoes")
    public String salvar(
            @RequestParam String idioma,
            @RequestParam(required = false) Boolean temaEscuro,
            @RequestParam(required = false) Boolean notificacaoEmail,
            HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        ConfiguracoesUsuario c = configDAO.findByUsuarioId(usuario.getId());
        if (c == null) {
            c = new ConfiguracoesUsuario();
            c.setUsuario(usuario);
        }

        c.setIdioma(idioma);
        c.setTemaEscuro(temaEscuro != null && temaEscuro);
        c.setNotificacaoEmail(notificacaoEmail != null && notificacaoEmail);

        configDAO.saveOrUpdate(c);

        return "redirect:/configuracoes?sucesso=true";
    }

    // POST - Alterar Email
    @PostMapping("/configuracoes/alterar-email")
    public String alterarEmail(@RequestParam String novoEmail,
                               HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        usuario.setEmail(novoEmail);
        usuarioDAO.update(usuario);

        // atualiza sessão
        session.setAttribute("usuario", usuario);

        return "redirect:/configuracoes?emailAlterado=true";
    }

    // POST - Alterar Senha
    @PostMapping("/configuracoes/alterar-senha")
    public String alterarSenha(
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha,
            HttpSession session
    ) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        // Se você usa hash, aqui precisa validar com BCrypt
        if (!usuario.getSenhaHash().equals(senhaAtual)) {
            return "redirect:/configuracoes?erroSenha=true";
        }

        usuario.setSenhaHash(novaSenha);
        usuarioDAO.update(usuario);

        session.setAttribute("usuario", usuario);

        return "redirect:/configuracoes?senhaAlterada=true";
    }
}
