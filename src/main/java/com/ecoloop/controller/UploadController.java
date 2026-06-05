package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        // Salva o arquivo numa pasta "uploads" na raiz do projeto (criada se nao existir).
        // transferTo exige caminho absoluto, senao resolve para a pasta temp do Tomcat.
        Path uploadDir = Paths.get("uploads").toAbsolutePath();
        Files.createDirectories(uploadDir);

        String nomeArquivo = System.currentTimeMillis() + "_"
                + (file.getOriginalFilename() == null ? "arquivo" : file.getOriginalFilename());
        file.transferTo(uploadDir.resolve(nomeArquivo).toFile());

        Integer userId = ((com.ecoloop.model.Usuario) session.getAttribute("usuario")).getId();

        MaterialEnviado m = new MaterialEnviado();
        m.setUsuarioId(userId);
        m.setDescricao(descricao);
        m.setCaminhoArquivo("/uploads/" + nomeArquivo);
        String contentType = file.getContentType();
        m.setTipoArquivo(contentType != null && contentType.startsWith("video") ? "video" : "foto");
        m.setStatus("pendente");

        Integer novoId = materialDAO.create(m);

        // Redireciona de volta para a tela de envio com o id do material recem-criado,
        // para que o JS chame a API /material/status/{id} e exiba a mensagem.
        return "redirect:/enviar?enviado=" + novoId;
    }
}
