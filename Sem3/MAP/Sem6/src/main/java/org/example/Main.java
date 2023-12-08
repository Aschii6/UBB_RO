package org.example;

import org.example.Domain.Movie;
import org.example.Repository.MovieDBRepository;
import org.example.Repository.Repository;

public class Main {
    public static void main(String[] args) {
        Repository<Long, Movie> repository = new MovieDBRepository("jdbc:postgresql://localhost:5432/moviesSem6",
                "postgres", "parola");

        System.out.println(repository.findOne(1L).get());

        /*Movie movie = new Movie("LOTR", "Idk", 2003);
        repository.save(movie);*/

        System.out.println(repository.findAll());

        repository.delete(2L);
        System.out.println(repository.findAll());
    }
}