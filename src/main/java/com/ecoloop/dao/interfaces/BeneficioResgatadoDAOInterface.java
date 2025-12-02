package com.ecoloop.dao.interfaces;

import com.ecoloop.model.BeneficioResgatado;
import java.util.List;

public interface BeneficioResgatadoDAOInterface {

    List<Integer> buscarIdsResgatados(Integer usuarioId);

    List<BeneficioResgatado> buscarResgatados(Integer usuarioId);

    void salvarResgate(Integer usuarioId, Integer beneficioId);
}
