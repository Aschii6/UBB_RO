import model.Configuration;
import persistence.ConfigurationOrmRepository;
import persistence.ConfigurationRepository;

public class Main {
    public static void main(String[] args) {
        ConfigurationRepository configurationRepository = new ConfigurationOrmRepository();

        configurationRepository.save(new Configuration(1, 1, "A clue"));
    }
}
