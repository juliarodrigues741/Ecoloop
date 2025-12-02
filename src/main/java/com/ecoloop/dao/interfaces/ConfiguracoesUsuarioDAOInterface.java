package com.ecoloop.dao.interfaces;

import com.ecoloop.model.ConfiguracoesUsuario;

import java.util.List;

public interface ConfiguracoesUsuarioDAOInterface {

    Integer create(ConfiguracoesUsuario c);

    boolean update(ConfiguracoesUsuario c);

    boolean delete(int id);

    ConfiguracoesUsuario findByUsuarioId(int usuarioId);

    List<ConfiguracoesUsuario> findAll();

    void saveOrUpdate(ConfiguracoesUsuario c);
}
