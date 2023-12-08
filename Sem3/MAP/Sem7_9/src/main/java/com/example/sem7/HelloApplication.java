package com.example.sem7;

import com.example.sem7.controllers.MoviesController;
import com.example.sem7.domain.Movie;
import com.example.sem7.repository.MovieDBRepository;
import com.example.sem7.repository.MovieRepository;
import com.example.sem7.repository.Repository;
import com.example.sem7.repository.paging.PagingRepository;
import com.example.sem7.service.MovieService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    /*@Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }*/

    @Override
    public void start(Stage stage) throws IOException {
        MovieRepository repository = new MovieDBRepository("jdbc:postgresql://localhost:5432/moviesSem6",
                "postgres", "parola");

        MovieService movieService = new MovieService(repository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/movies-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MoviesController moviesController = fxmlLoader.getController();
        moviesController.setMovieService(movieService);

        stage.setTitle("Movies");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}