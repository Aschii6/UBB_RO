package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.events.ServiceChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserPageController implements Observer<ServiceChangeEvent> {
    private Service service;
    private Long userId;
    private Stage stage;

    ObservableList<User> friendRequestsModel = FXCollections.observableArrayList();
    ObservableList<User> friendsModel = FXCollections.observableArrayList();
    ObservableList<Message> chatsModel = FXCollections.observableArrayList();

    @FXML
    TextField idTextField;
    @FXML
    TextField messageTextField;

    @FXML
    TableView<User> friendRequestsTableView;
    @FXML
    TableColumn<User, String> firstNameColumn1;
    @FXML
    TableColumn<User, String> lastNameColumn1;

    @FXML
    TableView<User> friendsTableView;
    @FXML
    TableColumn<User, String> firstNameColumn2;
    @FXML
    TableColumn<User, String> lastNameColumn2;

    @FXML
    ListView<Message> chatListView;

    public void setServiceAndId(Service service, Long userId, Stage stage) {
        this.service = service;
        service.addObserver(this);
        this.userId = userId;
        this.stage = stage;
        initModels();
    }

    @Override
    public void update(ServiceChangeEvent serviceChangeEvent) {
        initModels();
    }

    @FXML
    public void initialize() {
        friendRequestsTableView.setItems(friendRequestsModel);
        firstNameColumn1.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn1.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        friendsTableView.setItems(friendsModel);
        firstNameColumn2.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn2.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    void initModels() {
        friendRequestsModel.setAll(StreamSupport.stream(service.getAllFriendReqs(userId).spliterator(),
                false).collect(Collectors.toList()));

        friendsModel.setAll(StreamSupport.stream(service.getAllFriends(userId).spliterator(), false)
                .collect(Collectors.toList()));
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        try {
            Long idTo = Long.parseLong(idTextField.getText());
            service.sendFriendRequest(userId, idTo);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } finally {
            idTextField.clear();
        }
    }

    public void handleSendMessage(ActionEvent actionEvent) {
        Long idTo;
        try {
            idTo = Long.parseLong(idTextField.getText());
            idTextField.clear();
        } catch (Exception e) {
            idTextField.clear();
            MessageAlert.showErrorMessage(null, "Id must be Long.");
            return;
        }

        String messageText = messageTextField.getText();
        messageTextField.clear();

        if (messageText.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Message can't be empty.");
            return;
        }

        try {
            service.addMessage(userId, idTo, messageText);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleAcceptFriend(ActionEvent actionEvent) {
        User user = friendRequestsTableView.getSelectionModel().getSelectedItem();

        if (user != null) {
            service.acceptFriendRequest(userId, user.getId());
        }
        else
            MessageAlert.showErrorMessage(null, "No user selected");
    }

    public void handleDenyFriend(ActionEvent actionEvent) {
        User user = friendRequestsTableView.getSelectionModel().getSelectedItem();

        if (user != null) {
            service.rejectFriendRequest(userId, user.getId());
        }
        else
            MessageAlert.showErrorMessage(null, "No user selected");
    }
}
