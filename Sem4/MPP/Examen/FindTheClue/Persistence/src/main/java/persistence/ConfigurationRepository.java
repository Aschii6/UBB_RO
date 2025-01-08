package persistence;

import model.Configuration;

public interface ConfigurationRepository extends Repository<Long, Configuration> {
    Configuration getRandomConfiguration();
}
