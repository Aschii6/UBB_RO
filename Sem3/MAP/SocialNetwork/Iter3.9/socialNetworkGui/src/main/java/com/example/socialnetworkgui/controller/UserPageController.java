package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.events.ServiceChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserPageController implements Observer<ServiceChangeEvent> {
    private Service service;
    private Long userId;
    private Stage stage;

    ObservableList<User> friendRequestsModel = FXCollections.observableArrayList();
    ObservableList<User> friendsModel = FXCollections.observableArrayList();
    ObservableList<Message> chatModel = FXCollections.observableArrayList();

    @FXML
    TextField tagTextField;
    @FXML
    TextField messageTextField;

    @FXML
    TableView<User> friendRequestsTableView;
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
    Label replyingToLabel;

    @FXML
    TextField tagsTextField;

    User friendSelected = null;
    Message messageSelected = null;

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
                            if (message.getIdMessageThisRepliedTo() == 0)
                                setText(message.getMessageText());
                            else {
                                Message m = service.getMessage(message.getIdMessageThisRepliedTo());
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

        friendsTableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                        friendSelected = newValue;
                        if (friendSelected != null) {
                            chatModel.setAll(StreamSupport.stream(service.getAllMessagesBetweenTwoUsers(userId,
                                    friendSelected.getId()).spliterator(), false).collect(Collectors.toList()));
                        }
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
                replyingToLabel.setText("");
            } else {
                messageSelected = selectedMessage;
                replyingToLabel.setText("Replying to: " + messageSelected.getMessageText());
            }
        });
    }

    void initModels() {
        friendRequestsModel.setAll(StreamSupport.stream(service.getAllFriendReqs(userId).spliterator(),
                false).collect(Collectors.toList()));

        friendsModel.setAll(StreamSupport.stream(service.getAllFriends(userId).spliterator(), false)
                .collect(Collectors.toList()));

        if (friendSelected != null)
            chatModel.setAll(StreamSupport.stream(service.getAllMessagesBetweenTwoUsers(userId,
                    friendSelected.getId()).spliterator(), false).collect(Collectors.toList()));
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        String tag = tagTextField.getText();

        if (tag.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Tag can't be empty.");
            return;
        }
        tagTextField.clear();

        try {
            service.sendFriendRequest(userId, tag);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleSendMessage(ActionEvent actionEvent) {
        if (friendSelected == null) {
            MessageAlert.showErrorMessage(null, "No chat selected");
            messageTextField.clear();
            return;
        }

        String messageText = messageTextField.getText();
        messageTextField.clear();

        if (messageText.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Message can't be empty.");
            return;
        }

        Long idMessageThisRepliedTo = 0L;

        if (messageSelected != null) {
            idMessageThisRepliedTo = messageSelected.getId();
            chatListView.getSelectionModel().clearSelection();
            replyingToLabel.setText("");
        }

        try {
            service.addMessage(userId, friendSelected.getId(), messageText, idMessageThisRepliedTo);
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

    public void handleSendMultipleMessages(ActionEvent actionEvent) {
        String messageText = messageTextField.getText();
        messageTextField.clear();

        if (messageText.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Message can't be empty.");
            return;
        }

        List<String> tags = Arrays.stream(tagsTextField.getText().split(","))
                .map(String::strip)
                .toList();

        tagsTextField.clear();

        StringBuilder error = new StringBuilder();
        for (String tag : tags) {
            try {
                service.addMessage(userId, service.getUserByTag(tag).getId(), messageText, 0L);
            } catch (Exception e) {
                error.append(tag).append(" isn't a friend.\n");
            }
        }
        if (!error.toString().isEmpty()) {
            MessageAlert.showErrorMessage(null, error.toString());
        }
    }
}
