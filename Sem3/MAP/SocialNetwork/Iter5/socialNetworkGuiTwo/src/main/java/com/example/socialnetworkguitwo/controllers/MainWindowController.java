package com.example.socialnetworkguitwo.controllers;

import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.service.Service;
import com.example.socialnetworkguitwo.utils.events.UserChangeEvent;
import com.example.socialnetworkguitwo.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainWindowController implements Observer<UserChangeEvent> {
    private Service service;
    ObservableList<User> usersModel = FXCollections.observableArrayList();

    @FXML
    TableView<User> userTableView;
    @FXML
    TableColumn<User, Long> idColumn;
    @FXML
    TableColumn<User, String> firstNameColumn;
    @FXML
    TableColumn<User, String> lastNameColumn;
    @FXML
    TableColumn<User, String> tagColumn;

    public void setService(Service service) {
        this.service = service;
        service.getUserService().addObserver(this);
        initModel();
    }

    private void initModel() {
        usersModel.setAll(StreamSupport.stream(service.getAllUsers().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @FXML
    public void initialize() {
        userTableView.setItems(usersModel);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {

    }

    public void handleLogin(ActionEvent actionEvent) {
        User user = userTableView.getSelectionModel().getSelectedItem();

        if (user == null) {
            MessageAlert.showErrorMessage(null, "No user selected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/socialnetworkguitwo/views/user-page-view.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(user.getTag());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UserPageController userPageController = loader.getController();
            userPageController.setServiceAndId(service, user.getId(), dialogStage);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
