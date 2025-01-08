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
import java.util.List;


public class PlayerPageController implements IObserver {
    private IServices server;
    private Player player;

    private Game game;
    private LocalDateTime startTime;
    private List<Guess> guesses = new ArrayList<>();
    private Integer points = 0;
    private Integer correctGuesses = 0;

    private Integer lastTry = -1;
    private StackPane lastPane;

    @FXML
    private GridPane gameGP;

    ObservableList<Ranking> rankingsModel = FXCollections.observableArrayList();

    @FXML
    TableView<Ranking> rankingsTV;

    @FXML
    TableColumn<Ranking, String> aliasTC;
    @FXML
    TableColumn<Ranking, Integer> pointsTC;
    @FXML
    TableColumn<Ranking, Integer> durationTC;

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

        game = new Game(player, 0, null, configuration, null);

        startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        initModels();
    }

    public void setServer(IServices server) {
        this.server = server;
    }

    @FXML
    public void initialize() {
        aliasTC.setCellValueFactory(new PropertyValueFactory<>("alias"));
        pointsTC.setCellValueFactory(new PropertyValueFactory<>("points"));
        durationTC.setCellValueFactory(new PropertyValueFactory<>("duration"));

        rankingsTV.setItems(rankingsModel);

        pointsTC.setSortType(TableColumn.SortType.ASCENDING);
        durationTC.setSortType(TableColumn.SortType.ASCENDING);

        rankingsTV.getSortOrder().add(pointsTC);
        rankingsTV.getSortOrder().add(durationTC);

        for (Node node : gameGP.getChildren()) {
            if (node instanceof StackPane) {
                node.setOnMouseClicked(event -> handlePaneClick((StackPane) node));
            }
        }
    }

    private void handlePaneClick(StackPane pane) {
        Integer row = GridPane.getRowIndex(pane);
        Integer col = GridPane.getColumnIndex(pane);

        Integer pos = row * 5 + col;

        showGuess(pane, pos);

        if (lastTry == -1) {
            lastTry = pos;
            lastPane = pane;
            return;
        }

        Guess guess = new Guess(lastTry, pos);
        guesses.add(guess);

        if (game.getConfiguration().getConfiguration().get(lastTry) == game.getConfiguration().getConfiguration().get(pos)) {
            points -= 2;

            correctGuesses++;

            lastTry = -1;
            lastPane = null;
        } else {
            points += 3;

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        hideGuess(pane);
                        hideGuess(lastPane);

                        lastTry = -1;
                        lastPane = null;
                    });
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }).start();
        }

        if (correctGuesses == 5 || guesses.size() == 10) {
            LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Integer duration = (int) ChronoUnit.SECONDS.between(startTime, endTime);

            game.setPoints(points);
            game.setDuration(duration);
            game.setPositions(guesses);

            try {
                server.saveGame(game);
            } catch (Exception e) {
                System.out.println("Save game error: " + e.getMessage());
            }

            if (guesses.size() == 10) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        Platform.runLater(() -> {
                            for (Node node : gameGP.getChildren()) {
                                if (node instanceof StackPane) {
                                    Integer r = GridPane.getRowIndex(node);
                                    Integer c = GridPane.getColumnIndex(node);

                                    int p = r * 5 + c;

                                    for (Node n : ((StackPane) node).getChildren()) {
                                        if (n instanceof Label) {
                                            ((Label) n).setText(game.getConfiguration().getConfiguration().get(p).toString());
                                        }
                                    }
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }

            gameGP.setDisable(true);
        }
    }

    private void showGuess(StackPane pane, Integer pos) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setText(game.getConfiguration().getConfiguration().get(pos).toString());
            }
        }

        pane.setDisable(true);
    }

    private synchronized void hideGuess(StackPane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setText("");
            }
        }

        pane.setDisable(false);
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
            MessageAlert.showErrorMessage(null, e.getMessage());
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
