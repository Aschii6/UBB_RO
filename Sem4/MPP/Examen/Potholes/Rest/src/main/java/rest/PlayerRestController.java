package rest;

import model.Game;
import model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import persistence.GameRepository;
import persistence.PlayerRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/potholes/players")
public class PlayerRestController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @RequestMapping(value = "/{id}/games", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGamesForPlayer(@PathVariable long id) {
        System.out.println("Get games for player " + id);

        Optional<Player> opt = playerRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Player with given id doesnt exist", HttpStatus.NOT_FOUND);
        }

        List<Game> games = (List<Game>) gameRepository.findAllGamesForPlayerId(id);

        return new ResponseEntity<List<Game>>(games, HttpStatus.OK);
    }
}
