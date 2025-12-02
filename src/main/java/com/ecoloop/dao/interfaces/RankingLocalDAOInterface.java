package com.ecoloop.dao.interfaces;

import com.ecoloop.model.RankingLocal;
import java.util.List;

public interface RankingLocalDAOInterface {

    List<RankingLocal> getRankingLocal();

    Integer create(RankingLocal r);

    boolean update(RankingLocal r);

    RankingLocal findByUsuarioId(int usuarioId);

    List<RankingLocal> topN(int n);

    boolean delete(int id);
}
