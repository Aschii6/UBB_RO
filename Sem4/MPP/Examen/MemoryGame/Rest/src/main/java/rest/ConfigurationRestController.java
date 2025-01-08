package rest;

import model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.ConfigurationRepository;

import java.util.Optional;

@RestController
@RequestMapping("/memory-game/configurations")
public class ConfigurationRestController {
    @Autowired
    ConfigurationRepository configurationRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable long id) {
        Optional<Configuration> opt = configurationRepository.findOne(id);

        if (opt.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(opt.get());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateConfiguration(@PathVariable("id") long id, @RequestBody Configuration configuration){
        configuration.setId(id);

        if (configurationRepository.update(configuration).isEmpty())
            return ResponseEntity.ok("Configuration updated");
        else
            return ResponseEntity.badRequest().body("Configuration not found");
    }
}
