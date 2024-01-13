package com.example.socialnetworkguitwo.controllers;

import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.service.Service;
import com.example.socialnetworkguitwo.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Service service;

    @FXML
    TextField loginTagTF;
    @FXML
    TextField loginPasswordTF;
    @FXML
    TextField signupTagTF;
    @FXML
    TextField signupPasswordTF;
    @FXML
    TextField signupFirstNameTF;
    @FXML
    TextField signupLastNameTF;

    public void handleLogin(ActionEvent actionEvent) {
        String tag = loginTagTF.getText();
        String password = loginPasswordTF.getText();

        loginTagTF.clear();
        loginPasswordTF.clear();

        if (tag.isEmpty() || password.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Tag and password not filled");
            return;
        }

        try {
            User user = service.login(tag, password);

            openUserPage(user);
        } catch (ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleSignup(ActionEvent actionEvent) {
        String tag = signupTagTF.getText();
        String password = signupPasswordTF.getText();
        String firstName = signupFirstNameTF.getText();
        String lastName = signupLastNameTF.getText();

        signupTagTF.clear();
        signupPasswordTF.clear();
        signupFirstNameTF.clear();
        signupLastNameTF.clear();

        if (tag.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            MessageAlert.showErrorMessage(null, "All fields must be filled");
            return;
        }

        try {
            User user = service.signup(tag, password, firstName, lastName);

            openUserPage(user);
        } catch (ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void setService(Service service) {
        this.service = service;
    }

    private void openUserPage(User user) {
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
