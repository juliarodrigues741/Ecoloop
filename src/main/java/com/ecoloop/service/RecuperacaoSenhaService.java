package com.ecoloop.service;

import com.ecoloop.dao.TokenRedefinicaoDAO;
import com.ecoloop.dao.UsuarioDAO;
import com.ecoloop.model.TokenRedefinicao;
import com.ecoloop.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private TokenRedefinicaoDAO tokenDAO;

    // Gera o token de redefinição e retorna o link
    public String solicitarRedefinicao(String email) {
        tokenDAO.deletarExpirados();

        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null) {
            return null; // sinaliza que o e-mail não existe
        }

        String token = UUID.randomUUID().toString().replace("-", "");

        TokenRedefinicao tr = new TokenRedefinicao();
        tr.setUsuarioId(usuario.getId());
        tr.setToken(token);
        tr.setExpiracao(LocalDateTime.now().plusMinutes(30));
        tokenDAO.salvar(tr);

        return "http://localhost:8080/redefinir-senha?token=" + token;
    }

    // Valida se o token ainda pode ser usado
    public String validarToken(String token) {
        TokenRedefinicao tr = tokenDAO.findByToken(token);

        if (tr == null || tr.isUsado()) {
            return "Link inválido ou já utilizado.";
        }

        if (tr.getExpiracao().isBefore(LocalDateTime.now())) {
            return "Este link expirou. Solicite um novo.";
        }

        return null; // null = token válido
    }

    // Realiza a troca da senha
    public String redefinirSenha(String token, String novaSenha, String confirmarSenha) {

        if (!novaSenha.equals(confirmarSenha)) {
            return "As senhas não coincidem.";
        }

        if (novaSenha.length() < 6) {
            return "A senha deve ter pelo menos 6 caracteres.";
        }

        String erroToken = validarToken(token);
        if (erroToken != null) {
            return erroToken;
        }

        TokenRedefinicao tr = tokenDAO.findByToken(token);
        Usuario usuario = usuarioDAO.findById(tr.getUsuarioId());
        if (usuario == null) {
            return "Usuário não encontrado.";
        }

        usuario.setSenhaHash(novaSenha);
        usuarioDAO.update(usuario);
        tokenDAO.marcarComoUsado(token);

        return null; // null = sucesso
    }
}