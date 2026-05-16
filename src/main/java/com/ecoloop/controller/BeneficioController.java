package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.dao.BeneficioResgatadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Beneficio;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BeneficioController {

    private final BeneficioDAO beneficioDAO;
    private final UsuarioDAO usuarioDAO;
    private final BeneficioResgatadoDAO beneficioResgatadoDAO;

    public BeneficioController(BeneficioDAO beneficioDAO,
                               UsuarioDAO usuarioDAO,
                               BeneficioResgatadoDAO beneficioResgatadoDAO) {
        this.beneficioDAO = beneficioDAO;
        this.usuarioDAO = usuarioDAO;
        this.beneficioResgatadoDAO = beneficioResgatadoDAO;
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model, HttpSession session) {
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) return "redirect:/login";

        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());
        if (usuario == null) return "redirect:/login";

        session.setAttribute("usuario", usuario);

        List<Beneficio> todos = beneficioDAO.findAll();
        List<Integer> idsResgatados = beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());

        List<Beneficio> disponiveis = new ArrayList<>(todos);
        disponiveis.removeIf(b -> idsResgatados.contains(b.getId()));

        List<Beneficio> resgatados = beneficioResgatadoDAO.buscarResgatados(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("resgatados", resgatados);

        return "beneficios";
    }

    @GetMapping("/beneficios/resgatar/{id}")
    public String resgatar(@PathVariable Integer id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) return "redirect:/login";

        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());
        if (usuario == null) return "redirect:/login";

        Beneficio beneficio = beneficioDAO.findById(id);
        if (beneficio == null) {
            redirectAttributes.addFlashAttribute("erro", "Benefício não encontrado!");
            return "redirect:/beneficios";
        }

        if (beneficioResgatadoDAO.jaResgatado(usuario.getId(), beneficio.getId())) {
            redirectAttributes.addFlashAttribute("erro", "Você já resgatou este benefício!");
            return "redirect:/beneficios";
        }

        if (usuario.getPontos() == null || usuario.getPontos() < beneficio.getPontosNecessarios()) {
            redirectAttributes.addFlashAttribute("erro", "Pontos insuficientes!");
            return "redirect:/beneficios";
        }

        boolean resgatado = beneficioResgatadoDAO.salvarResgate(usuario.getId(), beneficio.getId());
        if (!resgatado) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível concluir o resgate.");
            return "redirect:/beneficios";
        }

        usuarioDAO.removePoints(usuario.getId(), beneficio.getPontosNecessarios());

        Usuario atualizado = usuarioDAO.findByEmail(usuario.getEmail());
        session.setAttribute("usuario", atualizado);

        redirectAttributes.addFlashAttribute("sucesso", "Benefício resgatado com sucesso!");
        return "redirect:/beneficios";
    }
}