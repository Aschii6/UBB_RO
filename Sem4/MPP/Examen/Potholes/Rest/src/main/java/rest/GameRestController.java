package rest;

import model.Game;
import model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.GameRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/potholes/games")
public class GameRestController {
    @Autowired
    GameRepository gameRepository;

    @RequestMapping(value = "/{id}/positions", method = RequestMethod.POST)
    public ResponseEntity<?> saveMove(@PathVariable("id") long id, @RequestBody Position position) {
        System.out.println("saving move");

        Optional<Game> opt = gameRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Game not found", HttpStatus.NOT_FOUND);
        }

        Game game = opt.get();
        List<Position> positions = game.getPositions();
        positions.add(position);
        game.setPositions(positions);

        Optional<Game> optUpdate = gameRepository.update(game);

        if (optUpdate.isEmpty()) {
            return new ResponseEntity<String>("Move saved", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Move not saved", HttpStatus.BAD_REQUEST);
    }
}
