package com.example.socialnetworkguitwo;

import com.example.socialnetworkguitwo.controllers.MainWindowController;
import com.example.socialnetworkguitwo.repo.FriendshipDbRepo;
import com.example.socialnetworkguitwo.repo.MessageDbRepo;
import com.example.socialnetworkguitwo.repo.UserDbRepo;
import com.example.socialnetworkguitwo.service.FriendshipService;
import com.example.socialnetworkguitwo.service.MessageService;
import com.example.socialnetworkguitwo.service.Service;
import com.example.socialnetworkguitwo.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/main-window-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        Service service = getService();

        MainWindowController mainWindowController = fxmlLoader.getController();
        mainWindowController.setService(service);

        primaryStage.setTitle("Social Network");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Service getService() {
        UserDbRepo userRepo = new UserDbRepo("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "parola");

        FriendshipDbRepo friendshipRepo = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "parola");

        MessageDbRepo messageDbRepo = new MessageDbRepo("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "parola");

        UserService userService = new UserService(userRepo);

        FriendshipService friendshipService = new FriendshipService(friendshipRepo);

        MessageService messageService = new MessageService(messageDbRepo);

        return new Service(userService, friendshipService, messageService);
    }

    public static void main(String[] args){
        launch();
    }
}
