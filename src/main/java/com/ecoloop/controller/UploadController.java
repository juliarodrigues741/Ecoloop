package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class UploadController {

    @Autowired
    private MaterialEnviadoDAO materialDAO;

    @GetMapping("/enviar")
    public String enviarPage(HttpSession session) {

        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }

        return "enviar";
    }

    @PostMapping("/enviar")
    public String enviarMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("descricao") String descricao,
            HttpSession session
    ) throws Exception {

        Usuario user = (Usuario) session.getAttribute("usuario");

        if (user == null) {
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            return "redirect:/enviar?erro=arquivo";
        }

        // Diretório real para salvar arquivos
        String uploadDir = System.getProperty("user.dir") + "/uploads";

        File pasta = new File(uploadDir);
        if (!pasta.exists()) pasta.mkdirs();

        // Caminho completo no sistema
        String caminhoFinal = uploadDir + "/" + file.getOriginalFilename();
        file.transferTo(new File(caminhoFinal));

        // Caminho público para acessar no navegador
        String caminhoWeb = "/uploads/" + file.getOriginalFilename();

        MaterialEnviado m = new MaterialEnviado();
        m.setUsuarioId(user.getId());
        m.setDescricao(descricao);
        m.setCaminhoArquivo(caminhoWeb);
        m.setTipoArquivo(file.getContentType().startsWith("video") ? "video" : "foto");
        m.setStatus("pendente");

        materialDAO.create(m);

        return "redirect:/dashboard";
    }
}
