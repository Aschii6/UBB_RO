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
import model.Game;
import model.Move;
import model.Player;
import model.Ranking;
import services.IObserver;
import services.IServices;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class PlayerPageController implements IObserver {
    private IServices server;
    private Player player;

    private Game game;
    private LocalDateTime startTime;
    private Character[] moves = new Character[9];

    private List<Move> moveList = new ArrayList<>();

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

        initModels();

        startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
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

        Arrays.fill(moves, ' ');

        for (Node node : gameGP.getChildren()) {
            if (node instanceof StackPane) {
                node.setOnMouseClicked(event -> handlePaneClick((StackPane) node));
            }
        }
    }

    private void handlePaneClick(StackPane pane) {
        Integer row = GridPane.getRowIndex(pane);
        Integer col = GridPane.getColumnIndex(pane);

        int pos = row * 3 + col;

        moves[pos] = 'x';

        moveList.add(new Move(pos, 'x'));

        for (Node n : pane.getChildren()) {
            if (n instanceof Label) {
                ((Label) n).setText("X");
            }
        }

        pane.setDisable(true);

        if (checkWin()) {
            gameGP.setDisable(true);
            saveGame(10);
        } else if (moveList.size() == 9) {
            gameGP.setDisable(true);
            saveGame(5);
        } else {
            makeComputerMove();
        }
    }

    private void makeComputerMove() {
        Random random = new Random();

        int pos = random.nextInt(9);

        while (moves[pos] != ' ') {
            pos = random.nextInt(9);
        }

        moves[pos] = 'o';

        moveList.add(new Move(pos, 'o'));

        for (Node node : gameGP.getChildren()) {
            if (node instanceof StackPane) {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);

                StackPane pane = null;
                if (row * 3 + col == pos) {
                    pane = (StackPane) node;

                    for (Node n : pane.getChildren()) {
                        if (n instanceof Label) {
                            ((Label) n).setText("O");
                        }
                    }

                    pane.setDisable(true);

                    if (checkWin()) {
                        gameGP.setDisable(true);
                        saveGame(-10);
                    } else if (moveList.size() == 9) {
                        gameGP.setDisable(true);
                        saveGame(5);
                    }

                    break;
                }
            }
        }
    }

    private void saveGame(int points) {
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Integer duration = (int) ChronoUnit.SECONDS.between(startTime, endTime);

        game = new Game(player, points, duration, moveList);

        try {
            server.saveGame(game);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (moves[i] == moves[i + 3] && moves[i + 3] == moves[i + 6] && moves[i] != ' ') {
                return true;
            }

            if (moves[i * 3] == moves[i * 3 + 1] && moves[i * 3 + 1] == moves[i * 3 + 2] && moves[i * 3] != ' ') {
                return true;
            }
        }

        if (moves[0] == moves[4] && moves[4] == moves[8] && moves[0] != ' ') {
            return true;
        }

        if (moves[2] == moves[4] && moves[4] == moves[6] && moves[2] != ' ') {
            return true;
        }

        return false;
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
