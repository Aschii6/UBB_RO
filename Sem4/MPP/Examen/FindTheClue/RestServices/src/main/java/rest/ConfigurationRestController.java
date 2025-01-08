package rest;

import model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.ConfigurationRepository;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/clue-game/configurations")
public class ConfigurationRestController {
    @Autowired
    ConfigurationRepository configurationRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Configuration> getAll() {
        System.out.println("Get configs");

        return (Collection<Configuration>) configurationRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Configuration configuration) {
        System.out.println("Creating config");

        Optional<Configuration> opt = configurationRepository.save(configuration);

        if (opt.isEmpty()) {
            return new ResponseEntity<String>("Create successful", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Create failed", HttpStatus.BAD_REQUEST);
    }
}
