package persistence;

import model.Game;
import model.Move;

import java.util.Optional;

public interface GameRepository extends Repository<Long, Game> {
    Iterable<Game> findAllWinsForPlayerId(Long id);
    Optional<Move> saveMove(Move move);
}
