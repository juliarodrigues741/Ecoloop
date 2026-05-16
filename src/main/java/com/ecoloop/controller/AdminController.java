package com.ecoloop.controller;

import com.ecoloop.dao.ConquistaDAO;
import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Conquista;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Controller
public class AdminController {

    private static final int PONTOS_APROVACAO = 50;

    private final UsuarioDAO usuarioDAO;
    private final MaterialEnviadoDAO materialDAO;
    private final UsuarioConquistaDAO usuarioConquistaDAO;
    private final ConquistaDAO conquistaDAO;

    public AdminController(UsuarioDAO usuarioDAO,
                           MaterialEnviadoDAO materialDAO,
                           UsuarioConquistaDAO usuarioConquistaDAO,
                           ConquistaDAO conquistaDAO) {
        this.usuarioDAO = usuarioDAO;
        this.materialDAO = materialDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
        this.conquistaDAO = conquistaDAO;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuario");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminHome(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        Usuario user = (Usuario) session.getAttribute("usuario");

        List<Usuario> ranking = usuarioDAO.findRankingSemAdmin().stream()
                .sorted(Comparator.comparing(
                        Usuario::getPontos,
                        Comparator.nullsFirst(Integer::compareTo)
                ).reversed())
                .toList();

        int totalUsuarios = usuarioDAO.countAllSemAdmin();
        int coletasPendentes = materialDAO.countPendentes();
        int totalPontos = ranking.stream()
                .map(Usuario::getPontos)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        model.addAttribute("usuario", user);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("coletasPendentes", coletasPendentes);
        model.addAttribute("totalPontos", totalPontos);
        model.addAttribute("ranking", ranking);
        model.addAttribute("listaPendentes", materialDAO.findAllPendentes());

        return "admin/dashboard";
    }

    @GetMapping("/admin/uploads")
    public String uploads(@RequestParam(defaultValue = "pendente") String filtro,
                          HttpSession session,
                          Model model) {

        if (!isAdmin(session)) return "redirect:/login";

        Usuario user = (Usuario) session.getAttribute("usuario");

        model.addAttribute("usuario", user);
        model.addAttribute("filtro", filtro);
        model.addAttribute("materiais", materialDAO.listarPorStatus(filtro));

        return "admin/uploads";
    }

    @PostMapping("/admin/uploads/{id}/aprovar")
    public String aprovar(@PathVariable int id,
                          @RequestParam(defaultValue = "pendente") String filtro,
                          HttpSession session) {

        if (!isAdmin(session)) return "redirect:/login";

        MaterialEnviado material = buscarMaterialPorId(id);
        if (material == null) {
            return "redirect:/admin/uploads?filtro=" + filtro;
        }

        if ("aprovado".equalsIgnoreCase(material.getStatus())) {
            return "redirect:/admin/uploads?filtro=" + filtro;
        }

        materialDAO.aprovar(id, PONTOS_APROVACAO, "Aprovado pelo admin");
        usuarioDAO.addPoints(material.getUsuarioId(), PONTOS_APROVACAO);

        liberarConquistasPorNivel(material.getUsuarioId());

        return "redirect:/admin/uploads?filtro=" + filtro;
    }

    @PostMapping("/admin/uploads/{id}/recusar")
    public String recusar(@PathVariable int id,
                          @RequestParam(defaultValue = "pendente") String filtro,
                          HttpSession session) {

        if (!isAdmin(session)) return "redirect:/login";

        MaterialEnviado material = buscarMaterialPorId(id);
        if (material == null) {
            return "redirect:/admin/uploads?filtro=" + filtro;
        }

        materialDAO.recusar(id, "Recusado pelo admin");

        return "redirect:/admin/uploads?filtro=" + filtro;
    }

    private MaterialEnviado buscarMaterialPorId(int id) {
        return Stream.of(
                    materialDAO.listarPorStatus("pendente"),
                    materialDAO.listarPorStatus("aprovado"),
                    materialDAO.listarPorStatus("recusado")
                )
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(m -> m.getId() != null && m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void liberarConquistasPorNivel(int usuarioId) {
        long totalAprovados = materialDAO.findByUsuarioId(usuarioId).stream()
                .filter(m -> m.getStatus() != null && "aprovado".equalsIgnoreCase(m.getStatus()))
                .count();

        List<Conquista> todas = conquistaDAO.findAll();

        for (Conquista conquista : todas) {
            if (conquista == null || conquista.getId() == null || conquista.getNivelRequerido() == null) {
                continue;
            }

            String nivel = conquista.getNivelRequerido().trim().toLowerCase();
            boolean deveLiberar = false;

            switch (nivel) {
                case "bronze":
                    deveLiberar = totalAprovados >= 1;
                    break;
                case "prata":
                    deveLiberar = totalAprovados >= 5;
                    break;
                case "ouro":
                    deveLiberar = totalAprovados >= 10;
                    break;
            }

            if (deveLiberar) {
                boolean adicionou = usuarioConquistaDAO.addConquista(usuarioId, conquista.getId());

                if (adicionou && conquista.getPontosRecompensa() != null && conquista.getPontosRecompensa() > 0) {
                    usuarioDAO.addPoints(usuarioId, conquista.getPontosRecompensa());
                }
            }
        }
    }

    private void concederConquista(int usuarioId, Conquista conquista) {
        boolean adicionou = usuarioConquistaDAO.addConquista(usuarioId, conquista.getId());

        if (adicionou && conquista.getPontosRecompensa() != null && conquista.getPontosRecompensa() > 0) {
            usuarioDAO.addPoints(usuarioId, conquista.getPontosRecompensa());
        }
    }
}