package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.dao.ConquistaDAO;
import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.NotificacaoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.dao.UsuarioDesafioDAO;
import com.ecoloop.dao.DesafioDAO;
import com.ecoloop.model.Conquista;
import com.ecoloop.model.Desafio;
import com.ecoloop.model.Usuario;
import com.ecoloop.model.UsuarioDesafio;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final UsuarioDAO usuarioDAO;
    private final MaterialEnviadoDAO materialDAO;
    private final NotificacaoDAO notificacaoDAO;
    private final BeneficioDAO beneficioDAO;
    private final UsuarioDesafioDAO usuarioDesafioDAO;
    private final DesafioDAO desafioDAO;
    private final ConquistaDAO conquistaDAO;
    private final UsuarioConquistaDAO usuarioConquistaDAO;

    public DashboardController(UsuarioDAO usuarioDAO,
                               MaterialEnviadoDAO materialDAO,
                               NotificacaoDAO notificacaoDAO,
                               BeneficioDAO beneficioDAO,
                               UsuarioDesafioDAO usuarioDesafioDAO,
                               DesafioDAO desafioDAO,
                               ConquistaDAO conquistaDAO,
                               UsuarioConquistaDAO usuarioConquistaDAO) {
        this.usuarioDAO = usuarioDAO;
        this.materialDAO = materialDAO;
        this.notificacaoDAO = notificacaoDAO;
        this.beneficioDAO = beneficioDAO;
        this.usuarioDesafioDAO = usuarioDesafioDAO;
        this.desafioDAO = desafioDAO;
        this.conquistaDAO = conquistaDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(u.getEmail());

        // Total kg reciclados
        Double totalKg = materialDAO.sumKgByUsuario(usuario.getId());
        if (totalKg == null) totalKg = 0.0;
        usuario.setTotalKg(totalKg);

        // Carregar desafios
        List<UsuarioDesafio> usuarioDesafios = usuarioDesafioDAO.listarDesafiosPorUsuario(usuario.getId());
        List<Desafio> desafios = usuarioDesafios.stream()
                .map(ud -> desafioDAO.buscarPorId(ud.getDesafioId()))
                .filter(d -> d != null)
                .collect(Collectors.toList());
        usuario.setDesafios(desafios);

        // Carregar conquistas
        List<Integer> idsConquistas = usuarioConquistaDAO.listarIdsConquistasPorUsuario(usuario.getId());
        List<Conquista> conquistas = conquistaDAO.findAllByIds(idsConquistas);

        // Definir a conquista principal (ex: a que tem mais pontos)
        Conquista principal = conquistas.stream()
                .max((c1, c2) -> Integer.compare(c1.getPontosRecompensa(), c2.getPontosRecompensa()))
                .orElse(null);

        usuario.setConquistaPrincipal(principal);

        // Adicionar ao model
        model.addAttribute("usuario", usuario);
        model.addAttribute("notificacoes", notificacaoDAO.findByUsuario(usuario.getId()));
        model.addAttribute("beneficios", beneficioDAO.findAll());
        model.addAttribute("uploads", materialDAO.findByUsuarioId(usuario.getId()));

        return "dashboard";
    }
}
