package rest;

import model.Game;
import model.GameDto;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import persistence.GameRepository;
import persistence.PlayerRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clue-game/players")
public class PlayerRestController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @RequestMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        System.out.println("Get player by id " + id);

        Optional<Player> opt = playerRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Entity not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Player>(opt.get(), HttpStatus.OK);
    }

    @RequestMapping("/{id}/games")
    public Collection<GameDto> getGames(@PathVariable long id) {
        System.out.println("Get games for player " + id);

        Optional<Player> opt = playerRepository.findOne(id);

        if (opt.isEmpty()) {
            return List.of();
        }

        List<Game> games = (List<Game>) gameRepository.findAllForPlayerId(id);

        List<GameDto> gameDtos = new ArrayList<>();

        for (Game game : games) {
            GameDto gameDto = new GameDto(game.getConfiguration().getClue(), game.getNoOfTries(), game.getGuesses());
            gameDto.setId(game.getId());

            gameDtos.add(gameDto);
        }

        return gameDtos;
    }
}
