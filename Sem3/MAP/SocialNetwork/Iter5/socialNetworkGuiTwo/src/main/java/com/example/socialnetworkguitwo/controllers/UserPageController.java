package com.example.socialnetworkguitwo.controllers;

import com.example.socialnetworkguitwo.domain.Message;
import com.example.socialnetworkguitwo.domain.Status;
import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.repo.paging.Page;
import com.example.socialnetworkguitwo.repo.paging.Pageable;
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

    private int pageSize = 2;
    private int currentPage1 = 0;
    private int currentPage2 = 0;

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
    @FXML
    TextField pageSizeTextField;

    @FXML
    Button prevButton1;
    @FXML
    Button prevButton2;
    @FXML
    Button nextButton1;
    @FXML
    Button nextButton2;

    @FXML
    Label messageSelectedLabel;
    @FXML
    Label pageNumberLabel1;
    @FXML
    Label pageNumberLabel2;
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
                messageSelectedLabel.setText("");
                chatListView.getSelectionModel().clearSelection();
            } else {
                messageSelected = selectedMessage;
                messageSelectedLabel.setText("Replying to: " + messageSelected.getMessageText());
            }
        });
    }

    private void initModels() {
        /*friendReqsModel.setAll(StreamSupport.stream(service.getAllUsersWithFriendReq(userId).spliterator(), false)
            .collect(Collectors.toList()));

        friendsModel.setAll(StreamSupport.stream(service.getAllFriends(userId).spliterator(), false)
                .collect(Collectors.toList()));*/

        Page<User> page1 = service.getPagedFriendshipsOfAUser(new Pageable(currentPage1, pageSize), userId,
                Status.PENDING);

        int maxPage1 = (int) Math.ceil((double) page1.getTotalNrOfElems() / pageSize) - 1;

        if (maxPage1 == -1)
            maxPage1 = 0;

        if (currentPage1 > maxPage1) {
            currentPage1 = maxPage1;

            page1 = service.getPagedFriendshipsOfAUser(new Pageable(currentPage1, pageSize), userId, Status.PENDING);
        }

        friendReqsModel.setAll(StreamSupport.stream(page1.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList()));

        prevButton1.setDisable(currentPage1 == 0);
        nextButton1.setDisable((currentPage1 + 1) * pageSize >= page1.getTotalNrOfElems());

        pageNumberLabel1.setText("Page " + (currentPage1 + 1) + "/" + (maxPage1 + 1));

        Page<User> page2 = service.getPagedFriendshipsOfAUser(new Pageable(currentPage2, pageSize), userId,
                Status.APPROVED);

        int maxPage2 = (int) Math.ceil((double) page2.getTotalNrOfElems() / pageSize) - 1;

        if (maxPage2 == -1)
            maxPage2 = 0;

        if (currentPage2 > maxPage2) {
            currentPage2 = maxPage2;

            page2 = service.getPagedFriendshipsOfAUser(new Pageable(currentPage2, pageSize), userId, Status.PENDING);
        }

        friendsModel.setAll(StreamSupport.stream(page2.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList()));

        prevButton2.setDisable(currentPage2 == 0);
        nextButton2.setDisable((currentPage2 + 1) * pageSize >= page2.getTotalNrOfElems());

        pageNumberLabel2.setText("Page " + (currentPage2 + 1) + "/" + (maxPage2 + 1));

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

        if (message != null) {
            chatListView.getSelectionModel().clearSelection();
            messageSelectedLabel.setText("");
        }

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

    public void handlePressPrev1(ActionEvent actionEvent) {
        currentPage1--;
        initModels();
    }

    public void handlePressNext1(ActionEvent actionEvent) {
        currentPage1++;
        initModels();
    }

    public void handlePressPrev2(ActionEvent actionEvent) {
        currentPage2--;
        initModels();
    }

    public void handlePressNext2(ActionEvent actionEvent) {
        currentPage2++;
        initModels();
    }

    public void handleSetPageNumber(ActionEvent actionEvent) {
        String pageNrStr = pageSizeTextField.getText();

        if (pageNrStr.isEmpty()) {
            MessageAlert.showErrorMessage(null, "No page size written");
            return;
        }

        int pageNr = 0;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Page size not number");
            return;
        } finally {
            pageSizeTextField.clear();
        }

        if (pageNr <= 0) {
            MessageAlert.showErrorMessage(null, "Page size must be positive");
            return;
        }

        pageSize = pageNr;
        currentPage1 = 0;
        currentPage2 = 0;
        initModels();
    }

    @Override
    public void update(ServiceEvent serviceEvent) {
        initModels();
    }
}
