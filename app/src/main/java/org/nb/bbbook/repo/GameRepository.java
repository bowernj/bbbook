package org.nb.bbbook.repo;

import org.nb.bbbook.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {

    // Doesn't work in redis
//    List<Game> findByDateBetween(LocalDate start, LocalDate end);

}
