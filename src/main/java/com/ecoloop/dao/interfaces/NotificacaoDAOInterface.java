package com.ecoloop.dao.interfaces;

import com.ecoloop.model.Notificacao;
import java.util.List;

public interface NotificacaoDAOInterface {

    Integer create(Notificacao n);

    List<Notificacao> findByUsuario(int usuarioId);

    boolean marcarComoLida(int id);

    boolean delete(int id);
}
