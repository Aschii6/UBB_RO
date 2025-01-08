package persistence.hibernate;

import model.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import persistence.ConfigurationRepository;

import java.util.Optional;

@Component
public class ConfigurationHibernateRepository implements ConfigurationRepository {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Configuration> findOne(Long id) {
        logger.traceEntry("findOne configuration with id {}", id);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuration where id=:idC", Configuration.class)
                    .setParameter("idC", id)
                    .uniqueResultOptional();
        } catch (Exception e) {

            logger.error(e);
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Configuration> findAll() {
        return null;
    }

    @Override
    public Optional<Configuration> save(Configuration configuration) {
        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(configuration));
            return Optional.empty();
        }
        catch (Exception e) {
            return Optional.of(configuration);
        }
    }

    @Override
    public Optional<Configuration> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> update(Configuration configuration) {
        logger.traceEntry("update configuration {}", configuration);

        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> {
                if (session.find(Configuration.class, configuration.getId()) != null) {
                    session.merge(configuration);
                    session.flush();
                }
            });

            logger.traceExit("updated successfully");
            return Optional.empty();
        }
        catch (Exception e) {
            logger.error(e);
            return Optional.of(configuration);
        }
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
