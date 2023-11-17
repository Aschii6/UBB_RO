package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {
    private Service service;

    private Stage stage;

    @FXML
    TextField idField;

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    public void handleAdd() {
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
            service.addUser(firstName, lastName, id);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

        stage.close();
    }

    public void setService(Service service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }
}
