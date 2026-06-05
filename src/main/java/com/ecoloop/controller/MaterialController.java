package com.ecoloop.controller;

import com.ecoloop.model.Usuario;
import com.ecoloop.service.MaterialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // Mensagem de status de um material enviado:
    // - pendente  -> "arquivo enviado, aguarde a aprovacao do adm"
    // - aprovado  -> "seu material foi aprovado pelo administrador"
    // - recusado  -> "seu material foi recusado"
    @GetMapping("/status/{id}")
    public List<String> statusMaterial(@PathVariable int id) {
        return materialService.mensagemStatus(id);
    }

    // Notificacoes de avaliacao (aprovado/recusado) do usuario logado.
    // Usado pela tela "Enviar" para avisar quando o admin avalia um material.
    @GetMapping("/notificacoes")
    public List<String> minhasNotificacoes(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return List.of();
        }
        return materialService.notificacoesUsuario(usuario.getId());
    }
}
