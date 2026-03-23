package com.ecoloop.controller;

import com.ecoloop.dao.ConnectionBD;
import com.ecoloop.model.MaterialEnviado;
import com.ecoloop.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/uploads")
public class AdminUploadController {

    @GetMapping
    public String listar(@RequestParam(defaultValue = "pendente") String filtro,
                         HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        List<MaterialEnviado> materiais = new ArrayList<>();
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM materiais_enviados WHERE status = ? ORDER BY data_envio DESC")) {
            ps.setString(1, filtro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialEnviado m = new MaterialEnviado();
                    m.setId(rs.getInt("id"));
                    m.setUsuarioId(rs.getInt("usuario_id"));
                    m.setDescricao(rs.getString("descricao"));
                    m.setTipoArquivo(rs.getString("tipo_arquivo"));
                    m.setCaminhoArquivo(rs.getString("caminho_arquivo"));
                    Timestamp t = rs.getTimestamp("data_envio");
                    if (t != null) m.setDataEnvio(t.toLocalDateTime());
                    m.setPontosGerados(rs.getInt("pontos_gerados"));
                    m.setStatus(rs.getString("status"));
                    materiais.add(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("materiais", materiais);
        model.addAttribute("filtro", filtro);
        model.addAttribute("usuario", usuario);
        return "admin/uploads";
    }

    @PostMapping("/{id}/aprovar")
    public String aprovar(@PathVariable int id,
                          @RequestParam(defaultValue = "pendente") String filtro) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE materiais_enviados SET status='aprovado', data_avaliacao=? WHERE id=?")) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/uploads?filtro=" + filtro;
    }

    @PostMapping("/{id}/recusar")
    public String recusar(@PathVariable int id,
                          @RequestParam(defaultValue = "pendente") String filtro) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE materiais_enviados SET status='recusado', data_avaliacao=? WHERE id=?")) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/uploads?filtro=" + filtro;
    }
}