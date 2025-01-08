package persistence.hibernate;

import model.Game;
import model.Move;
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
        logger.traceEntry("findOne for id {}", id);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Game where id =: idG", Game.class)
                    .setParameter("idG", id).uniqueResultOptional();
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
        logger.traceEntry("save Game {}", game);

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
    public Optional<Game> update(Game game) {
        logger.traceEntry("update game {}", game);

        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> session.merge(game));
            logger.trace("updated game {}", game);
            return Optional.empty();
        } catch (Exception e) {
            logger.error(e);
            return Optional.of(game);
        }
    }

    @Override
    public Iterable<Game> findAllWinsForPlayerId(Long id) {
        logger.traceEntry("findAll for player id {}", id);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Game where player.id=:idP and points = 10 order by duration desc", Game.class)
                    .setParameter("idP", id)
                    .getResultList();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public Optional<Move> saveMove(Move move) {
        logger.traceEntry("save move {}", move);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.persist(move);
            logger.trace("saved move {}", move);
            return Optional.of(move);
        } catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }
}
