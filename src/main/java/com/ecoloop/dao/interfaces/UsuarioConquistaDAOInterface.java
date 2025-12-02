package com.ecoloop.dao.interfaces;

import com.ecoloop.model.ConquistaDoUsuario;
import com.ecoloop.model.UsuarioConquista;

import java.util.List;

public interface UsuarioConquistaDAOInterface {

    List<Integer> listarIdsConquistasPorUsuario(int usuarioId);

    boolean addConquista(int usuarioId, int conquistaId);

    boolean removeConquista(int usuarioId, int conquistaId);

    List<UsuarioConquista> findByUsuario(int usuarioId);

    List<Integer> findConquistaIdsByUsuario(int usuarioId);

    void deleteAllByUsuario(int usuarioId);

    List<ConquistaDoUsuario> findConquistasCompletasByUsuario(int usuarioId);
}
