package com.ecoloop.dao.interfaces;

import com.ecoloop.model.Conquista;
import java.util.List;

public interface ConquistaDAOInterface {

    Integer create(Conquista c);

    boolean update(Conquista c);

    boolean delete(int id);

    Conquista findById(int id);

    List<Conquista> findAll();

    List<Conquista> findAllByIds(List<Integer> ids);
}
