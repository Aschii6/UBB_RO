package com.example.sem7.observer;

import com.example.sem7.domain.Movie;

public class MovieChangeEvent implements Event {
    private MovieChangeEventType type;
    private Movie oldMovie;
    private Movie newMovie;

    public MovieChangeEvent(MovieChangeEventType type, Movie oldMovie, Movie newMovie) {
        this.type = type;
        this.oldMovie = oldMovie;
        this.newMovie = newMovie;
    }

    public MovieChangeEventType getType() {
        return type;
    }

    public void setType(MovieChangeEventType type) {
        this.type = type;
    }

    public Movie getOldMovie() {
        return oldMovie;
    }

    public void setOldMovie(Movie oldMovie) {
        this.oldMovie = oldMovie;
    }

    public Movie getNewMovie() {
        return newMovie;
    }

    public void setNewMovie(Movie newMovie) {
        this.newMovie = newMovie;
    }
}
