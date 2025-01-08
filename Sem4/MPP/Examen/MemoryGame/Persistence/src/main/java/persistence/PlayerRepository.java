package persistence;

import model.Player;

import java.util.Optional;

public interface PlayerRepository extends Repository<Long, Player> {
    Optional<Player> login(Player player);
}
