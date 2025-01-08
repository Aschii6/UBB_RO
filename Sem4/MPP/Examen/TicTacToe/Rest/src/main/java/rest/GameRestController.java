package rest;

import model.Game;
import model.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.GameRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tic-tac-toe/games")
public class GameRestController {
    @Autowired
    GameRepository gameRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        System.out.println("Get game by id " + id);

        Optional<Game> opt = gameRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Entity not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Game>(opt.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/moves", method = RequestMethod.POST)
    public ResponseEntity<?> saveMove(@PathVariable("id") long id, @RequestBody Move move) {
        System.out.println("saving move");

        Optional<Game> opt = gameRepository.findOne(id);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Game not found", HttpStatus.NOT_FOUND);
        }

        Game game = opt.get();
        List<Move> moves = game.getMoves();
        moves.add(move);
        game.setMoves(moves);

        Optional<Game> optUpdate = gameRepository.update(game);

        if (optUpdate.isEmpty()) {
            return new ResponseEntity<String>("Move saved", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Move not saved", HttpStatus.BAD_REQUEST);
    }
}
