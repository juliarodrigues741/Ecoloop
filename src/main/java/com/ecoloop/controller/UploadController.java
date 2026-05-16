package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Controller
public class UploadController {

    private final MaterialEnviadoDAO materialDAO;

    public UploadController(MaterialEnviadoDAO materialDAO) {
        this.materialDAO = materialDAO;
    }

    @GetMapping({"/envios", "/enviar"})
    public String enviarPage(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "enviar";
    }

    @GetMapping("/uploads")
    public String uploadsUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        if ("ADMIN".equalsIgnoreCase(usuario.getRole())) {
            return "redirect:/admin/uploads";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("materiais", materialDAO.findByUsuarioId(usuario.getId()));
        return "uploads";
    }

    @PostMapping({"/upload", "/uploads"})
    public String enviarMaterial(@RequestParam("files") MultipartFile file,
                                 @RequestParam(value = "descricao", required = false) String descricao,
                                 @RequestParam(value = "material", required = false) String material,
                                 HttpSession session) throws Exception {

        Usuario user = (Usuario) session.getAttribute("usuario");
        if (user == null) return "redirect:/login";

        if (file == null || file.isEmpty()) {
            return "redirect:/enviar?erro=arquivo";
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
            return "redirect:/enviar?erro=tipo";
        }

        String uploadDir = System.getProperty("user.dir") + "/uploads";
        File pasta = new File(uploadDir);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        String nomeOriginal = file.getOriginalFilename();
        String extensao = "";

        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }

        String nomeArquivo = UUID.randomUUID() + extensao;
        String caminhoFinal = uploadDir + "/" + nomeArquivo;

        file.transferTo(new File(caminhoFinal));

        String caminhoWeb = "/uploads/" + nomeArquivo;

        MaterialEnviado m = new MaterialEnviado();
        m.setUsuarioId(user.getId());
        m.setDescricao((descricao == null || descricao.isBlank()) ? nomeOriginal : descricao);
        m.setMaterial((material == null || material.isBlank() || "Selecione o tipo".equalsIgnoreCase(material)) ? null : material);
        m.setCaminhoArquivo(caminhoWeb);
        m.setTipoArquivo(contentType.startsWith("video/") ? "video" : "foto");
        m.setStatus("pendente");
        m.setPontosGerados(0);
        m.setComentarioAvaliacao(null);

        materialDAO.create(m);

        return "redirect:/uploads";
    }
}