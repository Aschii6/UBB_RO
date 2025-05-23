package persistence.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties props;
    private static final Logger logger = LogManager.getLogger();
    private Connection connection = null;

    public JdbcUtils(Properties props) {
        this.props = props;
    }

    private Connection getNewConnection() {
        logger.traceEntry();

        String url = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String pass = props.getProperty("jdbc.pass");

        logger.info("trying to connect to database ... {}", url);
        logger.info("user: {}", user);
        logger.info("pass: {}", pass);

        Connection con = null;
        try {
            if (user != null && pass != null)
                con = DriverManager.getConnection(url, user, pass);
            else
                con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return con;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (connection == null || connection.isClosed())
                connection = getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(connection);
        return connection;
    }
}

