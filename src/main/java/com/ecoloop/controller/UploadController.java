package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    @Autowired
    private MaterialEnviadoDAO materialDAO;

    @GetMapping("/enviar")
    public String enviarPage(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        model.addAttribute("usuario", usuario);
        return "enviar";
    }

    @GetMapping("/uploads")
    public String uploadsPage(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        model.addAttribute("usuario", usuario);
        model.addAttribute("materiais", materialDAO.findByUsuarioId(usuario.getId()));
        return "uploads";
    }

    @PostMapping("/enviar")
    public String enviarMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "descricao", required = false, defaultValue = "") String descricao,
            @RequestParam(value = "material", required = false, defaultValue = "") String material,
            HttpSession session
    ) throws Exception {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        if (file.isEmpty()) {
            return "redirect:/enviar?erro=arquivo";
        }

        Path uploadDir = Paths.get("uploads").toAbsolutePath();
        Files.createDirectories(uploadDir);

        String nomeArquivo = System.currentTimeMillis() + "_"
                + (file.getOriginalFilename() == null ? "arquivo" : file.getOriginalFilename());
        file.transferTo(uploadDir.resolve(nomeArquivo).toFile());

        MaterialEnviado m = new MaterialEnviado();
        m.setUsuarioId(usuario.getId());
        m.setDescricao(descricao);
        m.setMaterial(material);
        m.setCaminhoArquivo("/uploads/" + nomeArquivo);

        String contentType = file.getContentType();
        m.setTipoArquivo(contentType != null && contentType.startsWith("video") ? "video" : "foto");
        m.setStatus("pendente");

        materialDAO.create(m);
        return "redirect:/uploads";
    }
}