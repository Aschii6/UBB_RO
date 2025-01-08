package server;

import networking.utils.AbstractServer;
import networking.utils.RpcConcurrentServer;
import networking.utils.ServerException;
import persistence.ConfigurationRepository;
import persistence.GameRepository;
import persistence.PlayerRepository;
import persistence.hibernate.ConfigurationHibernateRepository;
import persistence.hibernate.GameHibernateRepository;
import persistence.hibernate.PlayerHibernateRepository;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();

        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        PlayerRepository playerRepository = new PlayerHibernateRepository();
        GameRepository gameRepository = new GameHibernateRepository();
        ConfigurationRepository configurationRepository = new ConfigurationHibernateRepository();

        ServicesImpl serverImpl = new ServicesImpl(playerRepository, gameRepository, configurationRepository);

        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong port Number " + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);

        AbstractServer server = new RpcConcurrentServer(serverPort, serverImpl);

        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
