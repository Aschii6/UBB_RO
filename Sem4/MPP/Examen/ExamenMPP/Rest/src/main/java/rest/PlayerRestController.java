package rest;

import model.Game;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.GameRepository;
import persistence.PlayerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trap-game/players")
public class PlayerRestController {
    private final static Logger logger = LogManager.getLogger();

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;

    @RequestMapping(value = "/{id}/games", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGamesForPlayer(@PathVariable long id,  @RequestParam(value = "points", required = true) int points) {
        logger.traceEntry("getAll games for player with id {}", id);

        Optional<Player> opt = playerRepository.findOne(id);

        if (opt.isEmpty()) {
            logger.traceExit("Player not found");
            return new ResponseEntity<>("Player with given id doesn't exist", HttpStatus.NOT_FOUND);
        }

        if (points != 30) {
            return new ResponseEntity<>("Not implemented", HttpStatus.NOT_IMPLEMENTED);
        }

        List<Game> games = (List<Game>) gameRepository.findAllWinsForPlayerId(id);

        logger.traceExit("Returning {} games", games.size());

        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
