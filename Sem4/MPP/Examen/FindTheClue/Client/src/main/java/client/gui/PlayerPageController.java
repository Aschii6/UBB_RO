package client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.*;
import services.IObserver;
import services.IServices;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class PlayerPageController implements IObserver {
    private IServices server;
    private Player player;
    private Game game;

    @FXML
    GridPane gameGP;

    ObservableList<Ranking> rankingsModel = FXCollections.observableArrayList();

    @FXML
    TableView<Ranking> rankingsTV;
    @FXML
    TableColumn<Ranking, String> aliasTC;
    @FXML
    TableColumn<Ranking, Integer> triesTC;
    @FXML
    TableColumn<Ranking, LocalDateTime> startTimeTC;
    @FXML
    TableColumn<Ranking, String> clueTC;

    public PlayerPageController() {
    }

    public PlayerPageController(IServices server) {
        this.server = server;
    }

    public void setPlayer(Player player) {
        this.player = player;

        Configuration configuration;

        try {
            configuration = server.getConfiguration();
        } catch (Exception e) {
            System.out.println("Get configuration error: " + e.getMessage());
            return;
        }

        game = new Game(player, configuration, new ArrayList<>(), 0, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        initModels();
    }

    public void setServer(IServices server) {
        this.server = server;
    }

    @FXML
    public void initialize() {
        for (Node node : gameGP.getChildren()) {
            if (node instanceof StackPane) {
                node.setOnMouseClicked(event -> handlePaneClick((StackPane) node));
            }
        }

        rankingsTV.setItems(rankingsModel);

        aliasTC.setCellValueFactory(new PropertyValueFactory<>("alias"));
        triesTC.setCellValueFactory(new PropertyValueFactory<>("noOfTries"));
        startTimeTC.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        clueTC.setCellValueFactory(new PropertyValueFactory<>("clue"));

        triesTC.setSortType(TableColumn.SortType.ASCENDING);

        rankingsTV.getSortOrder().add(triesTC);
    }

    private void handlePaneClick(StackPane pane) {
        Integer row = GridPane.getRowIndex(pane);
        Integer col = GridPane.getColumnIndex(pane);

        Integer correctRow = game.getConfiguration().getRow();
        Integer correctCol = game.getConfiguration().getColumn();

        game.addGuess(new Guess(row, col));
        game.setNoOfTries(game.getNoOfTries() + 1);

        if ((row.equals(correctRow) && col.equals(correctCol))) {
            gameGP.setDisable(true);

            for (Node node : pane.getChildren()) {
                if (node instanceof Label) {
                    ((Label) node).setText(game.getConfiguration().getClue());
                }
            }

            try {
                server.saveGame(game);
            } catch (Exception e) {
                System.out.println("Send game error: " + e.getMessage());
            }
        } else {
            double dist = (double) Math.round(Math.sqrt((row - correctRow) * (row - correctRow) +
                    (col - correctCol) * (col - correctCol)) * 100) / 100;

            for (Node node : pane.getChildren()) {
                if (node instanceof Label) {
                    ((Label) node).setText(String.valueOf(dist));
                }
            }

            if (game.getNoOfTries() == 4) {
                for (Node node : gameGP.getChildren()) {
                    if (node instanceof StackPane) {
                        Integer r = GridPane.getRowIndex(node);
                        Integer c = GridPane.getColumnIndex(node);

                        if (r.equals(correctRow) && c.equals(correctCol)) {
                            for (Node n : ((StackPane) node).getChildren()) {
                                if (n instanceof Label) {
                                    ((Label) n).setText(game.getConfiguration().getClue());
                                }
                            }
                        }
                    }
                }

                gameGP.setDisable(true);

                game.setNoOfTries(10);
                try {
                    server.saveGame(game);
                } catch (Exception e) {
                    System.out.println("Send game error: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        Platform.runLater(this::initModels);
    }

    private void initModels() {
        try {
            Ranking[] rankings = server.getRankings();
            rankingsModel.setAll(rankings);

            rankingsTV.sort();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Error: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            server.logout(player, this);
        } catch (Exception e) {
            System.out.println("Logout error: " + e.getMessage());
        }
    }
}
