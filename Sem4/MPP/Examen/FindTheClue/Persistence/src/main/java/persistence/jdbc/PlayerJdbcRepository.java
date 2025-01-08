package persistence.jdbc;

import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import persistence.PlayerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

@Component
public class PlayerJdbcRepository implements PlayerRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(PlayerJdbcRepository.class.getName());

    public PlayerJdbcRepository(Properties props) {
        System.out.println("PlayerJdbcRepository");
        logger.info("Initialising PlayerJdbcRepository");
        this.jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Player> findOne(Long id) {
        logger.traceEntry("findOne with id {}", id);

        Connection con = jdbcUtils.getConnection();

        try (
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Players WHERE id = ?")
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String alias = rs.getString("alias");

                Player player = new Player(alias);
                player.setId(id);

                logger.traceExit("found {}", player);
                return Optional.of(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.traceExit("did not find player");
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
        logger.traceEntry("login with player {}", player);

        Connection con = jdbcUtils.getConnection();

        try (
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Players WHERE alias = ?")
        ) {
            ps.setString(1, player.getAlias());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");

                player.setId(id);

                logger.traceExit("found player");
                return Optional.of(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.traceExit("did not find player");
        return Optional.empty();
    }
}
