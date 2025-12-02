package com.ecoloop.controller;

import com.ecoloop.dao.*;
import com.ecoloop.model.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
    private final RankingLocalDAO rankingLocalDAO;

    public DashboardController(UsuarioDAO usuarioDAO,
                               MaterialEnviadoDAO materialDAO,
                               NotificacaoDAO notificacaoDAO,
                               BeneficioDAO beneficioDAO,
                               UsuarioDesafioDAO usuarioDesafioDAO,
                               DesafioDAO desafioDAO,
                               ConquistaDAO conquistaDAO,
                               UsuarioConquistaDAO usuarioConquistaDAO,
                               RankingLocalDAO rankingLocalDAO) {

        this.usuarioDAO = usuarioDAO;
        this.materialDAO = materialDAO;
        this.notificacaoDAO = notificacaoDAO;
        this.beneficioDAO = beneficioDAO;
        this.usuarioDesafioDAO = usuarioDesafioDAO;
        this.desafioDAO = desafioDAO;
        this.conquistaDAO = conquistaDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
        this.rankingLocalDAO = rankingLocalDAO;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        // 1. Usuário logado
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(u.getEmail());

        //2. Total de kg reciclados
        Double totalKg = materialDAO.sumKgByUsuario(usuario.getId());
        totalKg = (totalKg != null) ? totalKg : 0.0;
        usuario.setTotalKg(totalKg);

        //3. Calcular nível do usuário
        String nivelRequerido;
        if (totalKg >= 50) {
            nivelRequerido = "Ouro";
        } else if (totalKg >= 20) {
            nivelRequerido = "Prata";
        } else {
            nivelRequerido = "Bronze";
        }

        usuario.setNivel(nivelRequerido);

        // Atualiza no banco
        usuarioDAO.atualizarNivel_requerido(usuario.getId(), nivelRequerido);

        //4. Carregar desafios + progresso
        List<UsuarioDesafio> usuarioDesafios =
                usuarioDesafioDAO.listarDesafiosPorUsuario(usuario.getId());

        for (UsuarioDesafio ud : usuarioDesafios) {
            Desafio d = desafioDAO.buscarPorId(ud.getDesafioId());
            ud.setDesafio(d);

            int progressoAtual = usuarioDesafioDAO.calcularProgressoPorDesafio(
                    usuario.getId(),
                    ud.getDesafioId()
            );

            ud.setProgresso(progressoAtual);
        }

        //5. Carregar conquistas
        List<Integer> idsConquistas =
                usuarioConquistaDAO.listarIdsConquistasPorUsuario(usuario.getId());

        List<Conquista> conquistas =
                conquistaDAO.findAllByIds(idsConquistas);

        Conquista principal = conquistas.stream()
                .max((c1, c2) -> Integer.compare(
                        c1.getPontosRecompensa(),
                        c2.getPontosRecompensa()
                ))
                .orElse(null);

        usuario.setConquistaPrincipal(principal);

        //6. Notificações + pontos
        List<Notificacao> notificacoes =
                notificacaoDAO.findByUsuario(usuario.getId());

        for (Notificacao n : notificacoes) {
            RankingLocal rk = rankingLocalDAO.findByUsuarioId(n.getUsuarioId());
            if (rk != null) {
                n.setPontos(rk.getPontos());
            }
        }

        //7. Enviar dados para o HTML
        model.addAttribute("usuario", usuario);
        model.addAttribute("desafios", usuarioDesafios);
        model.addAttribute("notificacoes", notificacoes);
        model.addAttribute("beneficios", beneficioDAO.findAll());
        model.addAttribute("uploads", materialDAO.findByUsuarioId(usuario.getId()));

        return "dashboard";
    }
}
