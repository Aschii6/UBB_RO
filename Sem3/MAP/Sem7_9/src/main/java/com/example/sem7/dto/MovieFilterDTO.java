package com.example.sem7.dto;

import java.util.Optional;

public class MovieFilterDTO {
    private Optional<Integer> year = Optional.empty();
    private Optional<String> title = Optional.empty();
    private Optional<String> director = Optional.empty();

    public Optional<Integer> getYear() {
        return year;
    }

    public void setYear(Optional<Integer> year) {
        this.year = year;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getDirector() {
        return director;
    }

    public void setDirector(Optional<String> director) {
        this.director = director;
    }
}
