package com.example.socialnetworkguitwo.controllers;

import com.example.socialnetworkguitwo.domain.Message;
import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.service.Service;
import com.example.socialnetworkguitwo.utils.events.ServiceEvent;
import com.example.socialnetworkguitwo.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserPageController implements Observer<ServiceEvent> {
    private Service service;
    private Long userId;
    private Stage stage;

    ObservableList<User> friendReqsModel = FXCollections.observableArrayList();
    ObservableList<User> friendsModel = FXCollections.observableArrayList();
    ObservableList<Message> chatModel = FXCollections.observableArrayList();

    @FXML
    TableView<User> friendReqsTableView;
    @FXML
    TableColumn<User, String> firstNameColumn1;
    @FXML
    TableColumn<User, String> lastNameColumn1;
    @FXML
    TableColumn<User, String> tagColumn1;

    @FXML
    TableView<User> friendsTableView;
    @FXML
    TableColumn<User, String> firstNameColumn2;
    @FXML
    TableColumn<User, String> lastNameColumn2;
    @FXML
    TableColumn<User, String> tagColumn2;

    @FXML
    ListView<Message> chatListView;

    @FXML
    TextField tagTextField;
    @FXML
    TextField messageTextField;

    Message messageSelected = null;

    public void setServiceAndId(Service service, Long id, Stage dialogStage) {
        this.service = service;
        service.addObserver(this);
        this.userId = id;
        this.stage = dialogStage;
        initModels();
    }

    @FXML
    public void initialize() {
        friendReqsTableView.setItems(friendReqsModel);
        firstNameColumn1.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn1.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tagColumn1.setCellValueFactory(new PropertyValueFactory<>("tag"));

        friendsTableView.setItems(friendsModel);
        firstNameColumn2.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn2.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tagColumn2.setCellValueFactory(new PropertyValueFactory<>("tag"));

        chatListView.setItems(chatModel);
        chatListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<Message>() {
                    @Override
                    protected void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);

                        if (empty || message == null) {
                            setText(null);
                        } else {
                            if (message.getRepliedMessage() == null)
                                setText(message.getMessageText());
                            else {
                                Message m = message.getRepliedMessage();
                                setText("(replied to: " + m.getMessageText() + ") " + message.getMessageText());
                            }

                            if (message.getIdFrom().equals(userId)) {
                                setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                                setTextFill(Color.DARKBLUE);
                            } else {
                                setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                                setTextFill(Color.CRIMSON);
                            }
                        }
                    }
                };
            }
        });

        friendsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        friendsTableView.getSelectionModel().getSelectedItems().addListener(
                new ListChangeListener<User>() {
                    @Override
                    public void onChanged(Change<? extends User> change) {
                        List<User> selectedFriends = new ArrayList<>(friendsTableView.getSelectionModel().getSelectedItems());

                        if (!selectedFriends.isEmpty()) {
                            User user = friendsTableView.getSelectionModel().getSelectedItems().get(0);
                            chatModel.setAll(StreamSupport.stream(service.getAllMessagesBetweenTwoUsers(userId, user.getId())
                                    .spliterator(), false).collect(Collectors.toList()));
                        }
                        else
                            chatModel.clear();
                    }
                }
        );

        chatListView.setOnMouseClicked(event -> {
            Message selectedMessage = chatListView.getSelectionModel().getSelectedItem();

            if (selectedMessage == null || selectedMessage == messageSelected ||
                    Objects.equals(selectedMessage.getIdFrom(), userId))
            {
                messageSelected = null;
                chatListView.getSelectionModel().clearSelection();
            } else {
                messageSelected = selectedMessage;
            }
        });
    }

    private void initModels() {
        friendReqsModel.setAll(StreamSupport.stream(service.getAllUsersWithFriendReq(userId).spliterator(), false)
            .collect(Collectors.toList()));

        friendsModel.setAll(StreamSupport.stream(service.getAllFriends(userId).spliterator(), false)
                .collect(Collectors.toList()));

        if (!friendsTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            User user = friendsTableView.getSelectionModel().getSelectedItems().get(0);
            chatModel.setAll(StreamSupport.stream(service.getAllMessagesBetweenTwoUsers(userId, user.getId())
                    .spliterator(), false).collect(Collectors.toList()));
        }
        else
            chatModel.clear();
    }

    public void handleSendMessage(ActionEvent actionEvent) {
        if (friendsTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            MessageAlert.showErrorMessage(null, "No chat selected");
            return;
        }

        String messageText = messageTextField.getText();
        messageTextField.clear();

        if (messageText.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Message can't be empty.");
            return;
        }

        Message message = messageSelected;

        if (message != null)
            chatListView.getSelectionModel().clearSelection();

        if (friendsTableView.getSelectionModel().getSelectedItems().size() != 1)
            message = null;

        List<User> to = friendsTableView.getSelectionModel().getSelectedItems();

        try {
            service.addMessage(userId, to, messageText, message);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleSendFriendReq(ActionEvent actionEvent) {
        String tag = tagTextField.getText();

        if (tag.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Tag can't be empty.");
            return;
        }
        tagTextField.clear();

        try {
            service.addFriendReq(userId, tag);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleAcceptFriendReq(ActionEvent actionEvent) {
        User user = friendReqsTableView.getSelectionModel().getSelectedItem();

        if (user != null) {
            service.acceptFriendReq(userId, user.getId());
        }
        else
            MessageAlert.showErrorMessage(null, "No user selected");
    }

    public void handleDenyFriendReq(ActionEvent actionEvent) {
        User user = friendReqsTableView.getSelectionModel().getSelectedItem();

        if (user != null) {
            service.denyFriendReq(userId, user.getId());
        }
        else
            MessageAlert.showErrorMessage(null, "No user selected");
    }

    @Override
    public void update(ServiceEvent serviceEvent) {
        initModels();
    }
}
