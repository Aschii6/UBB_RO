package persistence.hibernate;

import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import persistence.PlayerRepository;

import java.util.Optional;

@Component
public class PlayerHibernateRepository implements PlayerRepository {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Player> findOne(Long id) {
        logger.traceEntry("findOne player for id {}", id);

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Player where id=:idP", Player.class)
                    .setParameter("idP", id)
                    .uniqueResultOptional();
        } catch (Exception e) {
            logger.error(e);
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Player> findAll() {
        return null;
    }

    @Override
    public Optional<Player> save(Player player) {
        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(player));
            return Optional.empty();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.of(player);
        }
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
            return Optional.empty();
        }
    }
}
