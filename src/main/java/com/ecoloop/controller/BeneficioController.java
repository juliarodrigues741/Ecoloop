package com.ecoloop.controller;

import com.ecoloop.model.Usuario;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Beneficio;
import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.dao.BeneficioResgatadoDAO;
import com.ecoloop.model.BeneficioResgatado;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/beneficios/resgatar/{id}")
    public String resgatar(@PathVariable Integer id, HttpSession session, Model model) {

        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());
        Beneficio beneficio = beneficioDAO.findById(id);

        if (beneficio == null) {
            model.addAttribute("erro", "Benefício não encontrado!");
            return "redirect:/beneficios";
        }

        // IDs resgatados pelo usuário
        List<Integer> idsResgatados = beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());

        if (idsResgatados.contains(beneficio.getId())) {
            model.addAttribute("erro", "Você já resgatou este benefício!");
            return "redirect:/beneficios";
        }

        if (usuario.getPontos() < beneficio.getPontosNecessarios()) {
            model.addAttribute("erro", "Pontos insuficientes!");
            return "redirect:/beneficios";
        }

        // Desconta pontos
        usuario.setPontos(usuario.getPontos() - beneficio.getPontosNecessarios());
        usuarioDAO.update(usuario);

        // Registra resgate
        beneficioResgatadoDAO.salvarResgate(usuario.getId(), beneficio.getId());

        // Atualiza sessão
        session.setAttribute("usuario", usuarioDAO.findByEmail(usuario.getEmail()));

        return "redirect:/beneficios";
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model, HttpSession session) {

        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());
        session.setAttribute("usuario", usuario);

        // Todos os benefícios
        List<Beneficio> todos = beneficioDAO.findAll();

        // IDs já resgatados
        List<Integer> idsResgatados = beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());

        // Disponíveis = todos menos resgatados
        List<Beneficio> disponiveis = new ArrayList<>(todos);
        disponiveis.removeIf(b -> idsResgatados.contains(b.getId()));

        // Benefícios completos resgatados
        List<BeneficioResgatado> resgates = beneficioResgatadoDAO.buscarResgatados(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("resgates", resgates);

        return "beneficios";
    }
}
