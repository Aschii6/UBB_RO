package persistence.hibernate;

import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import persistence.PlayerRepository;

import java.util.Optional;

public class PlayerHibernateRepository implements PlayerRepository {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Player> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Player> findAll() {
        return null;
    }

    @Override
    public Optional<Player> save(Player entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Player> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Player> update(Player entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Player> login(Player player) {
        logger.traceEntry("login player {}", player);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Player where alias=:alias", Player.class)
                    .setParameter("alias", player.getAlias())
                    .uniqueResultOptional();
        } catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }
}
