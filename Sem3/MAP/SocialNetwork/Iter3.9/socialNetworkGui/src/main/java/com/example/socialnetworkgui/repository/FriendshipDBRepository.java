package com.example.socialnetworkgui.repository;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Status;
import com.example.socialnetworkgui.domain.Tuple;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipDBRepository implements FriendshipRepository {
    private String url;
    private String username;
    private String password;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> idsTuple) {
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friendships WHERE" +
                        " (id1 = ? AND id2 = ?) OR (id1 = ? AND id2 = ?)")
        ) {
            preparedStatement.setLong(1, idsTuple.getLeft());
            preparedStatement.setLong(2, idsTuple.getRight());
            preparedStatement.setLong(3, idsTuple.getRight());
            preparedStatement.setLong(4, idsTuple.getLeft());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                int s = resultSet.getInt("status");
                Status status = switch (s){
                    case 0 -> Status.PENDING;
                    case 1 -> Status.APPROVED;
                    case 2 -> Status.REJECTED;
                    default -> Status.PENDING;
                };

                LocalDateTime localDateTime = resultSet.getTimestamp("friendsFrom").toLocalDateTime();

                Friendship friendship = new Friendship(idsTuple);
                friendship.setStatus(status);
                friendship.setDate(localDateTime);

                return Optional.of(friendship);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friendships")
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                int s = resultSet.getInt("status");
                Status status = switch (s){
                    case 0 -> Status.PENDING;
                    case 1 -> Status.APPROVED;
                    case 2 -> Status.REJECTED;
                    default -> Status.PENDING;
                };

                LocalDateTime localDateTime = resultSet.getTimestamp("friendsFrom").toLocalDateTime();

                Friendship friendship = new Friendship(new Tuple<>(id1, id2));
                friendship.setStatus(status);
                friendship.setDate(localDateTime);

                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {
        if (friendship == null)
            throw new IllegalArgumentException("friendship must not be null.\n");

        if (findOne(friendship.getId()).isPresent())
            return Optional.of(friendship);

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO friendships " +
                        "(id1, id2, status) VALUES (?,?,?)")
        ){
            preparedStatement.setLong(1, friendship.getId().getLeft());
            preparedStatement.setLong(2, friendship.getId().getRight());

            Integer s = switch (friendship.getStatus()){
                case PENDING -> 0;
                case APPROVED -> 1;
                case REJECTED -> 2;
            };

            preparedStatement.setInt(3, s);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 0 ? Optional.of(friendship) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> idsTuple) {
        Optional<Friendship> opt = this.findOne(idsTuple);

        if (opt.isPresent()){
            try(
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM friendships " +
                            "WHERE (id1 = ? AND id2 = ?) OR (id1 = ? AND id2 = ?)")
            ){
                preparedStatement.setLong(1, idsTuple.getLeft());
                preparedStatement.setLong(2, idsTuple.getRight());
                preparedStatement.setLong(3, idsTuple.getRight());
                preparedStatement.setLong(4, idsTuple.getLeft());

                int affectedRows = preparedStatement.executeUpdate();

                return affectedRows == 0 ? Optional.empty() : opt;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship friendship) {
        if (friendship == null)
            throw new IllegalArgumentException("friendship must not be null.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE friendships " +
                        "SET friendsfrom = ?, status = ? WHERE (id1 = ? AND id2 = ?) OR (id1 = ? AND id2 = ?)")
        ){
            preparedStatement.setTimestamp(1, Timestamp.valueOf(friendship.getDate()));

            Integer s = switch (friendship.getStatus()){
                case PENDING -> 0;
                case APPROVED -> 1;
                case REJECTED -> 2;
            };

            preparedStatement.setInt(2, s);
            preparedStatement.setLong(3, friendship.getId().getLeft());
            preparedStatement.setLong(4, friendship.getId().getRight());
            preparedStatement.setLong(5, friendship.getId().getRight());
            preparedStatement.setLong(6, friendship.getId().getLeft());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 0 ? Optional.of(friendship) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> getAllFriendshipsOfAUser(Long id) {
        return null;
    }
}
