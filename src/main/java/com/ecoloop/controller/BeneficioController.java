package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioDAO;
import com.ecoloop.dao.BeneficioResgatadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Beneficio;
import com.ecoloop.model.BeneficioResgatado;
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

    // ==========================================
    //              LISTAR BENEFÍCIOS
    // ==========================================
    @GetMapping("/beneficios")
    public String listarBeneficios(Model model, HttpSession session) {

        // 1. Verifica login
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) return "redirect:/login";

        // 2. Atualiza usuário via banco (igual dashboard)
        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());
        session.setAttribute("usuario", usuario);

        // 3. Busca todos os benefícios
        List<Beneficio> todos = beneficioDAO.findAll();

        // 4. IDs dos que já foram resgatados
        List<Integer> idsResgatados =
                beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());

        // 5. Calcula disponíveis
        List<Beneficio> disponiveis = new ArrayList<>(todos);
        disponiveis.removeIf(b -> idsResgatados.contains(b.getId()));

        // 6. Busca objetos completos dos resgatados
        List<BeneficioResgatado> resgates =
                beneficioResgatadoDAO.buscarResgatados(usuario.getId());

        // 7. Envia para o HTML
        model.addAttribute("usuario", usuario);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("resgates", resgates);

        return "beneficios";
    }

    // ==========================================
    //              RESGATAR BENEFÍCIO
    // ==========================================
    @GetMapping("/beneficios/resgatar/{id}")
    public String resgatar(@PathVariable Integer id,
                           HttpSession session,
                           RedirectAttributes ra) {

        // 1. Verifica login
        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");
        if (usuarioSessao == null) {
            ra.addFlashAttribute("mensagemErro", "Você precisa estar logado.");
            return "redirect:/login";
        }

        // 2. Atualiza usuário
        Usuario usuario = usuarioDAO.findByEmail(usuarioSessao.getEmail());

        // 3. Busca o benefício
        Beneficio beneficio = beneficioDAO.findById(id);
        if (beneficio == null) {
            ra.addFlashAttribute("mensagemErro", "Benefício não encontrado.");
            return "redirect:/beneficios";
        }

        // 4. Checa se já foi resgatado
        List<Integer> idsResgatados =
                beneficioResgatadoDAO.buscarIdsResgatados(usuario.getId());

        if (idsResgatados.contains(beneficio.getId())) {
            ra.addFlashAttribute("mensagemErro", "Você já resgatou este benefício.");
            return "redirect:/beneficios";
        }

        // 5. Checa pontos suficientes
        if (usuario.getPontos() < beneficio.getPontosNecessarios()) {
            ra.addFlashAttribute("mensagemErro", "Pontos insuficientes.");
            return "redirect:/beneficios";
        }

        // 6. Desconta pontos
        usuario.setPontos(usuario.getPontos() - beneficio.getPontosNecessarios());
        usuarioDAO.update(usuario);

        // 7. Salva resgate
        beneficioResgatadoDAO.salvarResgate(usuario.getId(), beneficio.getId());

        // 8. Atualiza sessão
        session.setAttribute("usuario", usuarioDAO.findByEmail(usuario.getEmail()));

        // 9. Mensagem de sucesso
        ra.addFlashAttribute("mensagemSucesso", "Benefício resgatado com sucesso! 🎉");

        return "redirect:/beneficios";
    }
}