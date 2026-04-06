package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.model.Beneficio;
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
public class AdminRecompensaController {

    private final BeneficioDAO beneficioDAO = new BeneficioDAO();

    @GetMapping("/adicionar-recompensa")
    public String adicionarRecompensaPage(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "admin/adicionar-recompensa";
    }

    @PostMapping("/adicionar-recompensa")
    public String adicionarRecompensa(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam String categoria,
            @RequestParam Integer pontosNecessarios,
            @RequestParam(required = false) String imagemUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario adminLogado = (Usuario) session.getAttribute("usuario");
        if (adminLogado == null) {
            return "redirect:/login";
        }

        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Nome da recompensa é obrigatório");
            return "redirect:/admin/adicionar-recompensa";
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Descrição é obrigatória");
            return "redirect:/admin/adicionar-recompensa";
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Categoria é obrigatória");
            return "redirect:/admin/adicionar-recompensa";
        }

        if (pontosNecessarios == null || pontosNecessarios < 0) {
            redirectAttributes.addFlashAttribute("erro", "Pontos necessários deve ser um valor válido");
            return "redirect:/admin/adicionar-recompensa";
        }

        // Criar nova recompensa
        Beneficio novaRecompensa = new Beneficio();
        novaRecompensa.setNome(nome);
        novaRecompensa.setDescricao(descricao);
        novaRecompensa.setCategoria(categoria);
        novaRecompensa.setPontosNecessarios(pontosNecessarios);
        novaRecompensa.setImagemUrl(imagemUrl);

        try {
            Integer id = beneficioDAO.create(novaRecompensa);
            if (id != null) {
                redirectAttributes.addFlashAttribute("sucesso",
                    "Recompensa '" + nome + "' cadastrada com sucesso!");
                return "redirect:/admin/adicionar-recompensa";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar recompensa");
                return "redirect:/admin/adicionar-recompensa";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar recompensa: " + e.getMessage());
            return "redirect:/admin/adicionar-recompensa";
        }
    }
}
