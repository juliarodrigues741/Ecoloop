package com.ecoloop.dao.interfaces;

import com.ecoloop.model.Desafio;
import java.util.List;

public interface DesafioDAOInterface {

    void adicionarDesafio(Desafio desafio);

    List<Desafio> listarDesafios();

    Desafio buscarPorId(int id);

    void atualizarDesafio(Desafio desafio);

    void removerDesafio(int id);
}
