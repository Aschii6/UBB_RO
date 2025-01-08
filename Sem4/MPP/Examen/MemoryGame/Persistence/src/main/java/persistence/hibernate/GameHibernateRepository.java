package persistence.hibernate;

import model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import persistence.GameRepository;

import java.util.Optional;

@Component
public class GameHibernateRepository implements GameRepository {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Optional<Game> findOne(Long id) {
        logger.traceEntry("findOne game with id {}", id);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Game where id=:idG", Game.class)
                    .setParameter("idG", id)
                    .uniqueResultOptional();
        } catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Game> findAll() {
        logger.traceEntry("findAll games");

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Game", Game.class).getResultList();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public Optional<Game> save(Game game) {
        logger.traceEntry("save game {}", game);

        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(game));
            logger.trace("saved game {}", game);
            return Optional.empty();
        } catch (Exception e) {
            logger.error(e);
            return Optional.of(game);
        }
    }

    @Override
    public Optional<Game> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> update(Game entity) {
        return Optional.empty();
    }
}
