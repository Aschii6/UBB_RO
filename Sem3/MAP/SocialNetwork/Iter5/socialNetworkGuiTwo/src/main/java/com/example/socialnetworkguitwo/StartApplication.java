package com.example.socialnetworkguitwo;

import com.example.socialnetworkguitwo.controllers.LoginController;
import com.example.socialnetworkguitwo.controllers.MainWindowController;
import com.example.socialnetworkguitwo.repo.FriendshipDbRepo;
import com.example.socialnetworkguitwo.repo.MessageDbRepo;
import com.example.socialnetworkguitwo.repo.UserDbRepo;
import com.example.socialnetworkguitwo.service.FriendshipService;
import com.example.socialnetworkguitwo.service.MessageService;
import com.example.socialnetworkguitwo.service.Service;
import com.example.socialnetworkguitwo.service.UserService;
import com.example.socialnetworkguitwo.utils.Encryption;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// parole: alex.pop - parola1, dan.negru - parola2, radu.popescu - parola3, mihai.ardelean - parola4
// adrian.cuza - parola5, paul.dobrescu - parola6, marin.marinescu - parola7, cosmin.padin - parola8
// cont.nou - parola10, alt.cont - parola11

public class StartApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/main-window-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        Service service = getService();

        MainWindowController mainWindowController = fxmlLoader.getController();
        mainWindowController.setService(service);

        primaryStage.setTitle("Social Network");
        primaryStage.setScene(scene);
        primaryStage.show();*/

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 450);

        Service service = getService();

        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);

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
