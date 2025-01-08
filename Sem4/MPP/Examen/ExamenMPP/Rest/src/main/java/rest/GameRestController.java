package rest;

import model.Game;
import model.Position;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.GameRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trap-game/games")
public class GameRestController {
    @Autowired
    GameRepository gameRepository;

    @RequestMapping(value = "/{id}/traps", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTraps(@PathVariable("id") long id, @RequestBody Position position) {
        System.out.println("Updating traps");

        if (position.getRow() < 0 || position.getRow() > 4 || position.getCol() < 0 || position.getCol() > 4) {
            return new ResponseEntity<>("Position outside of game bounds", HttpStatus.BAD_REQUEST);
        }

        Optional<Game> opt = gameRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<>("Game with given id doesn't exist", HttpStatus.NOT_FOUND);
        }

        Game game = opt.get();
        List<Position> traps = game.getTraps();

        if (traps.contains(position)) {
            traps.remove(position);
        } else {
            traps.add(position);
        }

        game.setTraps(traps);

        Optional<Game> optUpdate = gameRepository.update(game);

        if (optUpdate.isEmpty()) {
            return new ResponseEntity<>(game.getTraps(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Change failed to save", HttpStatus.BAD_REQUEST);
    }
}
