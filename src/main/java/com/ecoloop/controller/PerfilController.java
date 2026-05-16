package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class PerfilController {

    private final UsuarioDAO usuarioDAO;
    private static final String UPLOAD_DIR = "uploads/";

    public PerfilController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        Usuario logado = (Usuario) session.getAttribute("usuario");
        if (logado == null) return "redirect:/login";

        Usuario usuario = usuarioDAO.findByEmail(logado.getEmail());
        if (usuario == null) return "redirect:/login";

        session.setAttribute("usuario", usuario);
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil/foto")
    public String atualizarFoto(@RequestParam("foto") MultipartFile foto,
                                HttpSession session) {
        Usuario logado = (Usuario) session.getAttribute("usuario");
        if (logado == null) return "redirect:/login";

        if (foto == null || foto.isEmpty())
            return "redirect:/perfil?erro=arquivo-vazio";

        String contentType = foto.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            return "redirect:/perfil?erro=tipo-invalido";

        try {
            File pastaUpload = new File(UPLOAD_DIR);
            if (!pastaUpload.exists()) pastaUpload.mkdirs();

            String extensao = foto.getOriginalFilename() != null
                    ? foto.getOriginalFilename().substring(foto.getOriginalFilename().lastIndexOf("."))
                    : ".jpg";
            String nomeArquivo = "perfil_" + logado.getId() + "_" + UUID.randomUUID() + extensao;

            Path destino = Paths.get(UPLOAD_DIR + nomeArquivo);
            Files.write(destino, foto.getBytes());

            Usuario usuario = usuarioDAO.findByEmail(logado.getEmail());
            usuario.setFotoPerfil("/uploads/" + nomeArquivo);
            usuarioDAO.update(usuario);
            session.setAttribute("usuario", usuario);

        } catch (IOException e) {
            return "redirect:/perfil?erro=falha-upload";
        }

        return "redirect:/perfil?sucesso=true";
    }
}