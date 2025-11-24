package com.ecoloop.controller;
import com.ecoloop.model.Usuario;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.*;
import com.ecoloop.controller.*;
import com.ecoloop.dao.*;
import jakarta.servlet.http.HttpSession;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.model.Beneficio;
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

        // 1. Pega usuário da sessão
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) {
            return "redirect:/login";
        }

        // 2. Busca usuário atualizado do banco (igual dashboard)
        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());

        // 3. Busca o benefício
        Beneficio beneficio = beneficioDAO.findById(id);
        if (beneficio == null) {
            model.addAttribute("erro", "Benefício não encontrado!");
            return "redirect:/beneficios";
        }

        // 4. Verifica se já foi resgatado
        List<Integer> idsResgatados = beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());
        if (idsResgatados.contains(beneficio.getId())) {
            model.addAttribute("erro", "Você já resgatou este benefício!");
            return "redirect:/beneficios";
        }

        // 5. Verifica se tem pontos suficientes
        if (usuario.getPontos() < beneficio.getPontosNecessarios()) {
            model.addAttribute("erro", "Pontos insuficientes!");
            return "redirect:/beneficios";
        }

        // 6. Desconta os pontos
        usuario.setPontos(usuario.getPontos() - beneficio.getPontosNecessarios());
        usuarioDAO.update(usuario);

        // 7. Registra o resgate
        beneficioResgatadoDAO.salvarResgate(usuario.getId(), beneficio.getId());

        // 8. Atualiza sessão com usuário atualizado
        session.setAttribute("usuario", usuarioDAO.findByEmail(usuario.getEmail()));

        return "redirect:/beneficios";
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model, HttpSession session) {

        // 1. Pega usuário da sessão
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) return "redirect:/login";

        // 2. Busca usuário atualizado igual ao dashboard
        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());

        // 3. Restaura usuário atualizado na sessão
        session.setAttribute("usuario", usuario);

        // 4. Lista completa
        List<Beneficio> todos = beneficioDAO.findAll();

        // 5. IDs já resgatados
        List<Integer> idsResgatados =
                beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());

        // 6. Disponíveis = todos exceto resgatados
        List<Beneficio> disponiveis = new ArrayList<>(todos);
        disponiveis.removeIf(b -> idsResgatados.contains(b.getId()));

        // 7. Lista completa de resgatados
        List<Beneficio> resgatados =
                beneficioResgatadoDAO.buscarResgatados(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("resgatados", resgatados);

        return "beneficios";
    }
}


