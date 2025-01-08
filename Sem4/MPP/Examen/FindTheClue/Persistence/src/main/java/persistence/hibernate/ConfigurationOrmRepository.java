package persistence.hibernate;

import model.Configuration;
import persistence.ConfigurationRepository;

import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationOrmRepository implements ConfigurationRepository {
    private static final Logger logger = LogManager.getLogger(ConfigurationOrmRepository.class);

    @Override
    public Optional<Configuration> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Configuration> findAll() {
        logger.traceEntry("findAll");

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuration", Configuration.class).getResultList();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public Optional<Configuration> save(Configuration configuration) {
        logger.traceEntry("saving configuration {}", configuration);

        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(configuration));
            logger.trace("saved configuration {}", configuration);
            return Optional.empty();
        } catch (Exception e) {
            logger.error(e);
            return Optional.of(configuration);
        }
    }

    @Override
    public Optional<Configuration> delete(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> update(Configuration configuration) {
        return Optional.empty();
    }

    @Override
    public Configuration getRandomConfiguration() {
        logger.traceEntry("getRandomConfiguration");

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuration order by random()", Configuration.class)
                    .setMaxResults(1).uniqueResult();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }
}
