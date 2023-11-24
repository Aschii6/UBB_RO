package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.MainWindowController;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.repository.FriendshipDBRepository;
import com.example.socialnetworkgui.repository.MessageDBRepository;
import com.example.socialnetworkgui.repository.UserDBRepository;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.service.MessageService;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        UserDBRepository userRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "parola");

        FriendshipDBRepository friendshipRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/" +
                "socialnetwork", "postgres", "parola");

        MessageDBRepository messageRepo = new MessageDBRepository("jdbc:postgresql://localhost:5432/" +
                "socialnetwork", "postgres", "parola");

        UserService userService = new UserService(userRepo, new UserValidator());

        FriendshipService friendshipService = new FriendshipService(friendshipRepo);

        MessageService messageService = new MessageService(messageRepo);

        Service service = new Service(userService, friendshipService, messageService);

        MainWindowController mainWindowController = fxmlLoader.getController();
        mainWindowController.setService(service);

        stage.setTitle("Social Network");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
