package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminUsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping("/adicionar-usuario")
    public String adicionarUsuarioPage(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "admin/adicionar-usuario";
    }

    @PostMapping("/adicionar-usuario")
    public String adicionarUsuario(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String cpf,
            @RequestParam String cargo,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario adminLogado = (Usuario) session.getAttribute("usuario");
        if (adminLogado == null) {
            return "redirect:/login";
        }

        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Nome é obrigatório");
            return "redirect:/admin/adicionar-usuario";
        }

        if (email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "E-mail é obrigatório");
            return "redirect:/admin/adicionar-usuario";
        }

        if (senha == null || senha.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Senha é obrigatória");
            return "redirect:/admin/adicionar-usuario";
        }

        if (!senha.equals(confirmarSenha)) {
            redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem");
            return "redirect:/admin/adicionar-usuario";
        }

        // Verificar se o e-mail já existe
        Usuario usuarioExistente = usuarioDAO.findByEmail(email);
        if (usuarioExistente != null) {
            redirectAttributes.addFlashAttribute("erro", "E-mail já cadastrado no sistema");
            return "redirect:/admin/adicionar-usuario";
        }

        // Criar novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenhaHash(senha); // Senha em texto plano (projeto acadêmico)
        novoUsuario.setCpf(cpf);
        novoUsuario.setTelefone(telefone);
        novoUsuario.setNivel(cargo);
        novoUsuario.setPontos(0);
        novoUsuario.setFotoPerfil(null);

        try {
            Integer id = usuarioDAO.create(novoUsuario);
            if (id != null) {
                redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário");
                return "redirect:/admin/adicionar-usuario";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/admin/adicionar-usuario";
        }
    }
}
