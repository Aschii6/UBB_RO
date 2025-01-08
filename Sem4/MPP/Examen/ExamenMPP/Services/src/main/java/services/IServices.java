package services;

import model.Game;
import model.Player;
import model.Ranking;

import java.util.Optional;

public interface IServices {
    Optional<Player> login(Player player, IObserver client) throws Exception;
    void logout(Player player, IObserver client) throws Exception;

    void saveGame(Game game) throws Exception;
    Ranking[] getRankings() throws Exception;
}
