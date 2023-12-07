package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {
    private UserService userService;
    private Stage stage;

    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML
    TextField tagField;

    public void handleAdd() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String tag = tagField.getText();

        firstNameField.clear();
        lastNameField.clear();
        tagField.clear();

        try {
            userService.addUser(firstName, lastName, tag);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

        stage.close();
    }

    public void setService(UserService userService, Stage stage) {
        this.userService = userService;
        this.stage = stage;
    }
}
