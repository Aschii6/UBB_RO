package client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.Game;
import model.Player;
import model.Position;
import model.Ranking;
import services.IObserver;
import services.IServices;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PlayerPageController implements IObserver {
    private IServices server;
    private Player player;

    private Game game;
    private List<Position> traps = new ArrayList<>();
    private List<Position> positions = new ArrayList<>();
    private Integer points = 0;
    private LocalDateTime startTime;
    private Integer currentColumn = 0;

    @FXML
    GridPane gameGP;

    @FXML
    TableView<Ranking> rankingsTV;
    @FXML
    TableColumn<Ranking, String> aliasTC;
    @FXML
    TableColumn<Ranking, Integer> pointsTC;
    @FXML
    TableColumn<Ranking, Integer> durationTC;

    ObservableList<Ranking> rankingsModel = FXCollections.observableArrayList();


    public PlayerPageController() {
    }

    public PlayerPageController(IServices server) {
        this.server = server;
    }

    public void setPlayer(Player player) {
        this.player = player;
        
        initTraps();

        startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        initModels();
    }

    private void initTraps() {
        Random random = new Random();

        for (int col = 0; col <= 4; col++) {
            int row = random.nextInt(5);

            traps.add(new Position(row, col));
        }

        int row = random.nextInt(5);
        int col = random.nextInt(5);

        while (traps.contains(new Position(row, col))) {
            row = random.nextInt(5);
            col = random.nextInt(5);
        }

        traps.add(new Position(row, col));
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

        pointsTC.setSortType(TableColumn.SortType.DESCENDING);
        durationTC.setSortType(TableColumn.SortType.DESCENDING);

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

        if (!col.equals(currentColumn)) {
            return;
        }

        pane.setDisable(true);

        Position position = new Position(row, col);
        positions.add(position);

        if (traps.contains(position)) {
            highlightTraps();
            LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Integer duration = (int) ChronoUnit.SECONDS.between(startTime, endTime);

            try {
                server.saveGame(new Game(player, points, duration, traps, positions));
                gameGP.setDisable(true);
                return;
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        } else {
            pane.setStyle("-fx-background-color: green;");
            currentColumn++;
            points += currentColumn * 2;
        }

        if (currentColumn == 5) {
            highlightTraps();
            LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Integer duration = (int) ChronoUnit.SECONDS.between(startTime, endTime);
            gameGP.setDisable(true);

            try {
                server.saveGame(new Game(player, points, duration, traps, positions));
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }

    private void highlightTraps() {
        for (Node node : gameGP.getChildren()) {
            if (node instanceof StackPane) {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);

                Position position = new Position(row, col);

                if (traps.contains(position))
                    node.setStyle("-fx-background-color: red;");
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
