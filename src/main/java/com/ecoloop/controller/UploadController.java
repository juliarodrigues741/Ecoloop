package com.ecoloop.controller;

import com.ecoloop.dao.MaterialEnviadoDAO;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "enviar";
    }

    @PostMapping("/enviar")
    public String enviarMaterial(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("material") String tipoMaterial,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("pesoKg") Double pesoKg,
            HttpSession session,
            RedirectAttributes ra
    ) throws Exception {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            ra.addFlashAttribute("mensagemErro", "Você precisa estar logado.");
            return "redirect:/login";
        }

        if (files == null || files.length == 0) {
            ra.addFlashAttribute("mensagemErro", "Nenhum arquivo enviado.");
            return "redirect:/enviar";
        }

        String staticPath = System.getProperty("user.dir") +
                "/src/main/resources/static/uploads/";

        File pasta = new File(staticPath);
        if (!pasta.exists()) pasta.mkdirs();

        boolean peloMenosUm = false;

        for (MultipartFile file : files) {

            if (file.isEmpty()) continue;

            peloMenosUm = true;

            String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(staticPath + nomeArquivo);

            Files.write(caminho, file.getBytes());

            String caminhoWeb = "/uploads/" + nomeArquivo;

            MaterialEnviado m = new MaterialEnviado();
            m.setUsuarioId(usuarioId);
            m.setDescricao(descricao);
            m.setCaminhoArquivo(caminhoWeb);
            m.setTipoMaterial(tipoMaterial);
            m.setTipoArquivo(file.getContentType().startsWith("video") ? "video" : "foto");
            m.setStatus("pendente");
            m.setPesoKg(pesoKg);
            m.setPontosGerados(0);

            materialDAO.create(m);
        }

        if (!peloMenosUm) {
            ra.addFlashAttribute("mensagemErro", "Nenhum arquivo válido foi enviado.");
            return "redirect:/enviar";
        }

        ra.addFlashAttribute("mensagemSucesso", "Material enviado com sucesso! Aguarde a avaliação.");
        return "redirect:/enviar";
    }

    @GetMapping("/uploads")
    public String uploadsPage(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("uploads", materialDAO.findAllByUsuario(usuario.getId()));

        return "uploads";
    }
}
