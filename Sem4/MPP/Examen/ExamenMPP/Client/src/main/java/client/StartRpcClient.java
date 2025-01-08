package client;

import client.gui.LoginController;
import client.gui.PlayerPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.rpc.ServicesRpcProxy;
import services.IServices;

import java.util.Properties;

public class StartRpcClient extends Application {
    private Stage stage;
    private static int defaultTriathlonPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();

        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            clientProps.list(System.out);
        } catch (Exception e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultServer);

        int serverPort = defaultTriathlonPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultTriathlonPort);
        }

        IServices server = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/login-view.fxml"));
        Parent root = loader.load();

        LoginController ctrl = loader.getController();
        ctrl.setServer(server);

        FXMLLoader pLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/player-page-view.fxml"));
        Parent pRoot = pLoader.load();

        PlayerPageController playerPageController = pLoader.getController();
        playerPageController.setServer(server);

        ctrl.setPlayerPageController(playerPageController);
        ctrl.setParent(pRoot);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
