package com.ecoloop.dao.interfaces;

import com.ecoloop.model.Usuario;
import java.util.List;

public interface UsuarioDAOInterface {

    Usuario findByEmail(String email);

    Usuario findById(Integer id);

    List<Usuario> findAll();

    int countAll();

    boolean create(Usuario u);

    boolean update(Usuario u);

    boolean delete(Integer id);

    boolean addPoints(int userId, int pontos);

    boolean atualizarNivel_requerido(Integer id, String nivel);

}
