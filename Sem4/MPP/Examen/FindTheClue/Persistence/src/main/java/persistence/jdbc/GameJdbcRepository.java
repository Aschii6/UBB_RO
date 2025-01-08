package persistence.jdbc;

import model.Configuration;
import model.Game;
import model.Guess;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import persistence.GameRepository;
import persistence.hibernate.HibernateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class GameJdbcRepository implements GameRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger(GameJdbcRepository.class);

    public GameJdbcRepository(Properties props) {
        System.out.println("GameJdbcRepository");
        logger.info("Initialising GameJdbcRepository");
        this.jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Game> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Game> findAll() {
        logger.traceEntry("findAll");

        List<Game> games = new ArrayList<>();

        Connection con = jdbcUtils.getConnection();

        try (
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Games")
        ) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                Long playerId = rs.getLong("player_id");
                Long configurationId = rs.getLong("configuration_id");
                LocalDateTime startTime = LocalDateTime.parse(rs.getString("start_time"));
                Integer noOfTries = rs.getInt("no_of_tries");

                Configuration configuration = getConfiguration(configurationId);
                List<Guess> guesses = getGuessesForGameId(con, id);

                Player player = getPlayer(con, playerId);

                Game game = new Game(player, configuration, guesses, noOfTries, startTime);
                game.setId(id);

                games.add(game);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.traceExit("findAll found {} values", games.size());
        return games;
    }

    @Override
    public Optional<Game> save(Game game) {
        logger.traceEntry("save with value {}", game);

        Connection con = jdbcUtils.getConnection();

        try (
                PreparedStatement ps = con.prepareStatement("INSERT INTO Games(player_id, configuration_id, " +
                        "start_time, no_of_tries) VALUES (?, ?, ?, ?)");

                PreparedStatement psId = con.prepareStatement("SELECT last_insert_rowid()");

                PreparedStatement psG = con.prepareStatement("INSERT INTO Guesses(game_id, row, col) VALUES (?, ?, ?)")
        ) {
            ps.setLong(1, game.getPlayer().getId());
            ps.setLong(2, game.getConfiguration().getId());
            ps.setString(3, game.getStartTime().toString());
            ps.setInt(4, game.getNoOfTries());

            ps.executeUpdate();

            ResultSet rs = psId.executeQuery();

            long id;
            if (rs.next()) {
                id = rs.getLong(1);
            } else {
                throw new SQLException("No id was returned");
            }

            for (Guess guess : game.getGuesses()) {
                psG.setLong(1, id);
                psG.setInt(2, guess.getRow());
                psG.setInt(3, guess.getColumn());

                psG.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        logger.traceExit("save successful");
        return Optional.empty();
    }

    @Override
    public Optional<Game> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> update(Game entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Game> findAllForPlayerId(Long idPlayer) {
        logger.traceEntry("findAllForPlayerId with id {}", idPlayer);

        List<Game> games = new ArrayList<>();

        Connection con = jdbcUtils.getConnection();

        try (
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Games WHERE player_id = ?")
        ) {
            ps.setLong(1, idPlayer);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                Long configurationId = rs.getLong("configuration_id");
                Integer noOfTries = rs.getInt("no_of_tries");
                LocalDateTime startTime = LocalDateTime.parse(rs.getString("start_time"));

                Configuration configuration = getConfiguration(configurationId);
                List<Guess> guesses = getGuessesForGameId(con, id);

                Player player = getPlayer(con, idPlayer);

                Game game = new Game(player, configuration, guesses, noOfTries, startTime);
                game.setId(id);

                games.add(game);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.traceExit("findAllForPlayerId found {} values", games.size());

        return games;
    }

    private List<Guess> getGuessesForGameId(Connection con, Long id) {
        List<Guess> guesses = new ArrayList<>();

        try (
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Guesses WHERE game_id = ?")
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer row = rs.getInt("row");
                Integer column = rs.getInt("col");

                Guess guess = new Guess(row, column);

                guesses.add(guess);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return guesses;
    }

    private Configuration getConfiguration(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuration where id = :idC", Configuration.class)
                    .setParameter("idC", id)
                    .getSingleResult();
        }
    }

    private Player getPlayer(Connection con, Long id) {
        try (
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Players WHERE id = ?")
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String alias = rs.getString("alias");

                Player player = new Player(alias);

                player.setId(id);

                return player;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
