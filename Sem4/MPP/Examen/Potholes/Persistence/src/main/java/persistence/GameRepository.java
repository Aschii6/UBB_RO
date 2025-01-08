package persistence;

import model.Game;

public interface GameRepository extends Repository<Long, Game> {
    Iterable<Game> findAllGamesForPlayerId(long id);
}
