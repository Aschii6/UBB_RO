package org.example.Repository;

import org.example.Domain.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDBRepository implements Repository<Long, Movie> {
    private String url;
    private String username;
    private String password;

    public MovieDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Movie> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM movies WHERE id = ?")
        ){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                Integer year = resultSet.getInt("year");

                Movie movie = new Movie(title, director, year);
                movie.setId(id);

                return Optional.ofNullable(movie);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM movies")
        ){
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                Integer year = resultSet.getInt("year");

                Movie movie = new Movie(title, director, year);
                movie.setId(id);

                movies.add(movie);
            }
            return movies;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> save(Movie entity) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO movies " +
                "(title, director, year) VALUES (?,?,?)");
        ) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getDirector());
            preparedStatement.setInt(3, entity.getYear());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0){
                return Optional.empty();
            }
            else return Optional.ofNullable(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> delete(Long id) {
        Optional<Movie> opt = this.findOne(id);
        int affectedRows = 0;

        if (opt.isPresent()) {
            try(Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM movies WHERE id = ?");
            ) {
                preparedStatement.setLong(1, id);

                affectedRows = preparedStatement.executeUpdate();

                return affectedRows == 0 ? Optional.empty() : opt;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Movie> update(Movie entity) {
        try(Connection connection= DriverManager.getConnection(this.url,this.username,this.password);
            PreparedStatement preparedStatement=connection.prepareStatement("UPDATE movies SET title = ?, director = ?, year = ? WHERE id = ?");

        ){
            preparedStatement.setString(1,entity.getTitle());
            preparedStatement.setString(2,entity.getDirector());
            preparedStatement.setInt(3,entity.getYear());
            preparedStatement.setLong(4,entity.getId());
            int affectedRows=preparedStatement.executeUpdate();
            return affectedRows == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
