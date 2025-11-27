	package com.ecoloop.controller;

import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioDAO usuarioDAO;

    public AdminUsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("lista", usuarioDAO.findAll());
        return "admin/usuarios/index";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("usuario", usuarioDAO.findById(id));
        return "admin/usuarios/form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Usuario usuario,
            @RequestParam("foto") MultipartFile foto,
            HttpSession session
    ) {
        if (!isAdmin(session)) return "redirect:/login";

        try {
            if (foto != null && !foto.isEmpty()) {

                String nomeArquivo = System.currentTimeMillis() + "_" + foto.getOriginalFilename();

                Path pasta = Paths.get("src/main/resources/static/uploads/");
                Files.createDirectories(pasta);

                Path caminhoFinal = pasta.resolve(nomeArquivo);
                Files.write(caminhoFinal, foto.getBytes());

                usuario.setFotoPerfil("/uploads/" + nomeArquivo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (usuario.getId() == null)
            usuarioDAO.create(usuario);
        else
            usuarioDAO.update(usuario);

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        usuarioDAO.delete(id);
        return "redirect:/admin/usuarios";
    }
}
