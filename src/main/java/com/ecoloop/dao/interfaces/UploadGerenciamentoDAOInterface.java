package com.ecoloop.dao.interfaces;

import com.ecoloop.model.MaterialEnviado;
import java.util.List;

public interface UploadGerenciamentoDAOInterface {

    List<MaterialEnviado> listarPorStatus(String status);

    boolean aprovar(int id, int pontos, String comentario);

    boolean recusar(int id, String comentario);
}
