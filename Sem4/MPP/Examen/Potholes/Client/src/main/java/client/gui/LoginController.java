package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Player;
import services.IServices;

import java.util.Optional;

public class LoginController {
    IServices server;
    private PlayerPageController playerPageController;
    private Player crtPlayer;
    private Parent mainParent;

    @FXML
    TextField aliasTF;

    public void setServer(IServices server) {
        this.server = server;
    }

    public void setParent(Parent mainParent) {
        this.mainParent = mainParent;
    }

    public void setPlayerPageController(PlayerPageController playerPageController) {
        this.playerPageController = playerPageController;
    }

    public void handleLogin(ActionEvent actionEvent) {
        String alias = aliasTF.getText();

        aliasTF.clear();

        if (alias.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Alias must be completed!");
            return;
        }

        crtPlayer = new Player(alias);

        try {
            Optional<Player> opt = server.login(crtPlayer, playerPageController);

            if (opt.isEmpty()) {
                MessageAlert.showErrorMessage(null, "Authentication failed.");
                return;
            }

            crtPlayer = opt.get();

            Stage stage = new Stage();
            stage.setTitle(crtPlayer.getAlias());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest((WindowEvent e) -> {
                try {
                    playerPageController.logout();
                } catch (Exception ex) {
                    MessageAlert.showErrorMessage(null, ex.getMessage());
                }
            });

            stage.show();
            playerPageController.setPlayer(crtPlayer);

            Stage currentStage = (Stage) aliasTF.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
