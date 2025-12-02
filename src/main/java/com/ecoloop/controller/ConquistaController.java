package com.ecoloop.controller;

import com.ecoloop.dao.ConquistaDAO;
import com.ecoloop.dao.RankingLocalDAO;
import com.ecoloop.dao.UsuarioConquistaDAO;
import com.ecoloop.model.Conquista;
import com.ecoloop.model.RankingLocal;
import com.ecoloop.model.Usuario;
import com.ecoloop.model.UsuarioConquista;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ConquistaController {

    private final ConquistaDAO conquistaDAO;
    private final UsuarioConquistaDAO usuarioConquistaDAO;
    private final RankingLocalDAO rankingLocalDAO;

    public ConquistaController(ConquistaDAO conquistaDAO,
                               UsuarioConquistaDAO usuarioConquistaDAO,
                               RankingLocalDAO rankingLocalDAO) {
        this.conquistaDAO = conquistaDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
        this.rankingLocalDAO = rankingLocalDAO;
    }

    @GetMapping("/conquistas")
    public String conquistas(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Todas as conquistas disponíveis
        List<Conquista> todas = conquistaDAO.findAll();

        // Conquistas obtidas pelo usuário
        List<UsuarioConquista> minhas = usuarioConquistaDAO.findByUsuario(usuario.getId());
        Set<Integer> idsObtidos = minhas.stream()
                .map(UsuarioConquista::getConquistaId)
                .collect(Collectors.toSet());

        // Quantidade para exibição "X/Y"
        model.addAttribute("totalConquistas", todas.size());
        model.addAttribute("conquistasObtidas", idsObtidos.size());
        model.addAttribute("minhasConquistas", minhas);

        // Ranking: já vem ordenado do DAO
        List<RankingLocal> ranking = rankingLocalDAO.getRankingLocal();

        // Atribui posições (1, 2, 3...)
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setPosicao(i + 1);
        }

        model.addAttribute("ranking", ranking);

        // Descobre posição do usuário no ranking
        int minhaPosicao = ranking.stream()
                .filter(r -> r.getUsuarioId() != null && r.getUsuarioId().equals(usuario.getId()))
                .map(RankingLocal::getPosicao)
                .findFirst()
                .orElse(0);

        model.addAttribute("minhaPosicao", minhaPosicao);

        // Envia conquistas para a view
        model.addAttribute("conquistas", todas);
        model.addAttribute("obtidas", idsObtidos);

        return "conquistas";
    }
}
