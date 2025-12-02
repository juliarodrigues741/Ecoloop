package com.ecoloop.dao.interfaces;

import com.ecoloop.model.Beneficio;
import java.util.List;

public interface BeneficioDAOInterface {

    Integer create(Beneficio b);

    boolean update(Beneficio b);

    boolean delete(int id);

    Beneficio findById(int id);

    List<Beneficio> findAll();
}
