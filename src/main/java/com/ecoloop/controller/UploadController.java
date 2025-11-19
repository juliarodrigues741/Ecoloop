package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class UploadController {

    private final MaterialEnviadoDAO materialDAO = new MaterialEnviadoDAO();

    @GetMapping("/enviar")
    public String enviarPage() {
        return "enviar";
    }

    @PostMapping("/enviar")
    public String enviarMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("descricao") String descricao,
            HttpSession session
    ) throws Exception {

        if (file.isEmpty()) {
            return "redirect:/enviar?erro=arquivo";
        }

        String caminho = "src/main/resources/static/uploads/" + file.getOriginalFilename();
        file.transferTo(new File(caminho));

        Integer userId = ((com.ecoloop.model.Usuario) session.getAttribute("usuario")).getId();

        MaterialEnviado m = new MaterialEnviado();
        m.setUsuarioId(userId);
        m.setDescricao(descricao);
        m.setCaminhoArquivo("/uploads/" + file.getOriginalFilename());
        m.setTipoArquivo(file.getContentType().startsWith("video") ? "video" : "foto");
        m.setStatus("pendente");

        materialDAO.create(m);

        return "redirect:/dashboard";
    }
}
