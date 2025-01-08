package rest;

import model.Game;
import model.GameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import persistence.GameRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/memory-game/games")
public class GameRestController {
    @Autowired
    GameRepository gameRepository;

    @RequestMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        System.out.println("Get game by id " + id);

        Optional<Game> opt = gameRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Entity not found", HttpStatus.NOT_FOUND);
        }

        Game game = opt.get();

        GameDto gameDto = new GameDto(game.getPlayer().getAlias(), game.getPositions(), game.getConfiguration().getConfiguration(), game.getPoints());
        gameDto.setId(game.getId());

        return new ResponseEntity<GameDto>(gameDto, HttpStatus.OK);
    }
}
