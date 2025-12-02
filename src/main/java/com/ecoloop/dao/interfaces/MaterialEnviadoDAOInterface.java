package com.ecoloop.dao.interfaces;

import com.ecoloop.model.MaterialEnviado;
import java.util.List;

public interface MaterialEnviadoDAOInterface {

    Integer create(MaterialEnviado m);

    MaterialEnviado findById(int id);

    List<MaterialEnviado> findByUsuarioId(int usuarioId);

    List<MaterialEnviado> findAllPendentes();

    int countPendentes();

    Double sumKgByUsuario(int usuarioId);

    boolean aprovar(int id, int pontos, String comentario, Double pesoKg);

    boolean recusar(int id, String comentario);

    boolean delete(int id);
}
