package server;


import model.Game;
import model.Player;
import model.Ranking;
import persistence.GameRepository;
import persistence.PlayerRepository;
import services.IObserver;
import services.IServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesImpl implements IServices {
    PlayerRepository playerRepository;
    GameRepository gameRepository;

    private final int defaultThreadsNo = 5;
    private Map<String, IObserver> loggedClients;

    public ServicesImpl(PlayerRepository playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;

        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<Player> login(Player player, IObserver client) throws Exception {
        System.out.println("Login request: " + player.getAlias());

        Optional<Player> opt = playerRepository.login(player);

        if (opt.isPresent()) {
            if (loggedClients.get(player.getAlias()) != null) {
                throw new Exception("Player already logged in");
            }

            loggedClients.put(player.getAlias(), client);

            return opt;
        } else {
            throw new Exception("Player not registered");
        }
    }

    @Override
    public void logout(Player player, IObserver client) throws Exception {
        IObserver localClient = loggedClients.remove(player.getAlias());

        if (localClient == null) {
            throw new Exception("Player not logged in");
        }
    }

    @Override
    public void saveGame(Game game) throws Exception {
        gameRepository.save(game);

        for (IObserver client : loggedClients.values()) {
            client.update();
        }
    }

    @Override
    public Ranking[] getRankings() throws Exception {
        List<Game> games = (List<Game>) gameRepository.findAll();

        List<Ranking> rankings = new ArrayList<>();

        for (Game game : games) {
            Ranking ranking = new Ranking(game.getPlayer().getAlias(), game.getPoints(), game.getDuration());
            ranking.setId(game.getId());

            rankings.add(ranking);
        }

        return rankings.toArray(new Ranking[0]);
    }
}
