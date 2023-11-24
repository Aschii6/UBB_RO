package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateUserController {
    private UserService userService;

    private Stage stage;

    @FXML
    TextField idField;

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    public void handleEdit() {
        long id = 0;
        try {
            id = Long.parseLong(idField.getText());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Id trebuie sa fie de tip Long");
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        idField.clear();
        firstNameField.clear();
        lastNameField.clear();

        try {
            userService.updateUser(firstName, lastName, id);
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

