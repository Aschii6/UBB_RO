package persistence;

import model.Game;

public interface GameRepository extends Repository<Long, Game> {
    Iterable<Game> findAllForPlayerId(Long id);
}
