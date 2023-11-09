package Repo;

import Domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDBRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<User> findOne(Long id) {
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")
        ){
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                User user = new User(firstName, lastName);
                user.setId(id);

                return Optional.of(user);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users")
        ){
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                User user = new User(firstName, lastName);
                user.setId(id);

                users.add(user);
            }
            return users;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User user) {
        if (user == null)
            throw new IllegalArgumentException("user must not be null.\n");

        if (findOne(user.getId()).isPresent())
            throw new RepositoryException("Id already used.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users " +
                        "(id, firstName, lastName) VALUES (?,?,?)")
        ){
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 0 ? Optional.of(user) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long id) {
        /*Optional<User> opt = this.findOne(id);

        if (opt.isPresent()){
            try(
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")
            ){
                preparedStatement.setLong(1, id);

                int affectedRows = preparedStatement.executeUpdate();

                return affectedRows == 0 ? Optional.empty() : opt;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }*/
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User user) {
        if (user == null)
            throw new IllegalArgumentException("user must not be null.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET firstName = ?," +
                        " lastName = ? WHERE id = ?")
        ){
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setLong(3, user.getId());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 0 ? Optional.of(user) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
