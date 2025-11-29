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
    private final RankingLocalDAO rankingLocalDAO; // <-- agora final

    // ⬅️ ADICIONE O rankingLocalDAO ao construtor sem mudar a estrutura
    public ConquistaController(ConquistaDAO conquistaDAO, 
                               UsuarioConquistaDAO usuarioConquistaDAO,
                               RankingLocalDAO rankingLocalDAO) {
        this.conquistaDAO = conquistaDAO;
        this.usuarioConquistaDAO = usuarioConquistaDAO;
        this.rankingLocalDAO = rankingLocalDAO; // <-- agora funciona
    }

    @GetMapping("/conquistas")
    public String conquistas(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Todas as conquistas
        List<Conquista> todas = conquistaDAO.findAll();

        // Conquistas do usuário
        List<UsuarioConquista> minhas = usuarioConquistaDAO.findByUsuario(usuario.getId());

        // IDs das conquistas já obtidas
        Set<Integer> idsObtidos = minhas.stream()
                .map(UsuarioConquista::getConquistaId)
                .collect(Collectors.toSet());

        // Quantidade — para exibir "4/8"
        model.addAttribute("totalConquistas", todas.size());
        model.addAttribute("conquistasObtidas", idsObtidos.size());

        // Para mostrar datas das conquistas no card
        model.addAttribute("minhasConquistas", minhas);

        // Ranking Local
        List<RankingLocal> ranking = rankingLocalDAO.getRankingLocal();
        model.addAttribute("ranking", ranking);

        // Encontrar posição do usuário
        int posicao = ranking.stream()
                .map(RankingLocal::getUsuarioId) // <-- CORRIGIDO
                .toList()
                .indexOf(usuario.getId()) + 1;

        model.addAttribute("minhaPosicao", posicao);

        // Enviar conquistas para a view
        model.addAttribute("conquistas", todas);
        model.addAttribute("obtidas", idsObtidos);

        return "conquistas";
    }

}
