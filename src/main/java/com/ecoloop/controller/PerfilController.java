package com.ecoloop.controller;

import com.ecoloop.dao.BeneficioResgatadoDAO;
import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.BeneficioResgatado;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PerfilController {

    private final UsuarioDAO usuarioDAO;
    private final MaterialEnviadoDAO materialEnviadoDAO;
    private final BeneficioResgatadoDAO beneficioResgatadoDAO;

    public PerfilController(UsuarioDAO usuarioDAO,
                            MaterialEnviadoDAO materialEnviadoDAO,
                            BeneficioResgatadoDAO beneficioResgatadoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.materialEnviadoDAO = materialEnviadoDAO;
        this.beneficioResgatadoDAO = beneficioResgatadoDAO;
    }

    @GetMapping("/perfil")
    public String perfil(Model model,
                         HttpSession session,
                         HttpServletRequest request,
                         @RequestParam(value = "aba", required = false, defaultValue = "reciclagens") String aba) {

        Usuario logado = (Usuario) session.getAttribute("usuario");
        if (logado == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(logado.getEmail());
        if (usuario == null) {
            return "redirect:/login";
        }

        if (usuario.getFotoPerfil() == null || usuario.getFotoPerfil().isBlank()) {
            usuario.setFotoPerfil("/imagens/default-user.png");
        }

        //soma total reciclado
        Double totalKg = materialEnviadoDAO.sumKgByUsuario(usuario.getId());

     // --- reciclagens aprovadas ---
        List<MaterialEnviado> reciclagens = materialEnviadoDAO.findByUsuarioId(usuario.getId())
                .stream()
                .filter(m -> "aprovado".equalsIgnoreCase(m.getStatus()))
                .toList();

        //total de uploads aprovados
        int totalUploadsAprovados = reciclagens.size();
        model.addAttribute("totalUploadsAprovados", totalUploadsAprovados);

        //calcular CO2 baseado no peso total
        double co2Economizado = totalKg * 1.5; // ajuste o multiplicador se quiser

        int arvoresSalvas = (int) (totalKg / 20);

        List<BeneficioResgatado> resgates = beneficioResgatadoDAO.buscarResgatados(usuario.getId());
        
     // definir pontos por nível
        int nivelAtual = 5; // 
        int nivelAlvo = nivelAtual + 1; // 
        int pontosParaProximoNivel = 600; // 

        int pontosAtuais = usuario.getPontos();
        int pontosFaltando = Math.max(pontosParaProximoNivel - pontosAtuais, 0);
        double percentualProgresso = Math.min((pontosAtuais * 100.0) / pontosParaProximoNivel, 100.0);

        
        model.addAttribute("nivelAlvo", nivelAlvo);
        model.addAttribute("pontosFaltando", pontosFaltando);
        model.addAttribute("percentualProgresso", percentualProgresso);
        model.addAttribute("arvoresSalvas", arvoresSalvas);
        model.addAttribute("co2Economizado", co2Economizado);
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalKg", totalKg); 
        model.addAttribute("reciclagens", reciclagens);
        model.addAttribute("resgates", resgates);
        model.addAttribute("aba", aba);
        model.addAttribute("requestURI", request.getRequestURI());

        return "perfil";
    }

}
