package com.example.socialnetworkguitwo.repo;

import com.example.socialnetworkguitwo.domain.Friendship;
import com.example.socialnetworkguitwo.domain.Status;
import com.example.socialnetworkguitwo.domain.Tuple;
import com.example.socialnetworkguitwo.repo.paging.Page;
import com.example.socialnetworkguitwo.repo.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipDbRepo implements FriendshipRepository {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipDbRepo(String url, String username, String password) {
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
                LocalDateTime localDateTime = resultSet.getTimestamp("friendsFrom").toLocalDateTime();

                Friendship friendship = new Friendship(idsTuple);
                friendship.setStatus(statusFromInt(s));
                friendship.setTimeSent(localDateTime);

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
                LocalDateTime localDateTime = resultSet.getTimestamp("friendsFrom").toLocalDateTime();

                Friendship friendship = new Friendship(new Tuple<>(id1, id2));
                friendship.setStatus(statusFromInt(s));
                friendship.setTimeSent(localDateTime);

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
                        "SET friendsFrom = ?, status = ? WHERE (id1 = ? AND id2 = ?) OR (id1 = ? AND id2 = ?)")
        ){
            preparedStatement.setTimestamp(1, Timestamp.valueOf(friendship.getTimeSent()));

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
    public Iterable<Friendship> getAllFriendReqs(Long userId) {
        List<Friendship> friendReqs = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friendships " +
                        "WHERE id2 = ? AND status = 0")
        ) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id1 = resultSet.getLong("id1");

                Friendship friendship = new Friendship(new Tuple<>(id1, userId));

                friendReqs.add(friendship);
            }
            return friendReqs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> getAllFriendships(Long userId) {
        List<Friendship> friendships = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friendships " +
                        "WHERE (id1 = ? OR id2 = ?) AND status = 1")
        ) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                Friendship friendship = new Friendship(new Tuple<>(id1, id2));
                friendship.setStatus(Status.APPROVED);

                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Friendship> getPagedFriendshipsOfAUser(Pageable pageable, Long userId, Status status) {
        List<Friendship> friendships = new ArrayList<>();

        String pageSQL = "SELECT * FROM friendships WHERE (id1 = ? OR id2 = ?)";
        String countSQL = "SELECT COUNT(*) AS count FROM friendships WHERE (id1 = ? OR id2 = ?)";

        if (status != null) {
            pageSQL += " AND status = ?";
            countSQL += " AND status = ?";
        }

        pageSQL += " LIMIT ? OFFSET ?";

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement pageStatement = connection.prepareStatement(pageSQL);
                PreparedStatement countStatement = connection.prepareStatement(countSQL)
        ) {
            int paramIndex = 1;
            pageStatement.setLong(paramIndex++, userId);
            pageStatement.setLong(paramIndex++, userId);

            if (status != null) {
                Short s = switch (status){
                    case PENDING -> 0;
                    case APPROVED -> 1;
                    case REJECTED -> 2;
                };
                pageStatement.setShort(paramIndex++, s);
                countStatement.setShort(3, s);
            }
            pageStatement.setInt(paramIndex++, pageable.getPageSize());
            pageStatement.setInt(paramIndex, pageable.getPageSize() * pageable.getPageNr());

            countStatement.setLong(1, userId);
            countStatement.setLong(2, userId);

            try (
            ResultSet pageRS = pageStatement.executeQuery();
            ResultSet countRS = countStatement.executeQuery();
            ) {
                int count = 0;
                if (countRS.next())
                    count = countRS.getInt("count");

                while (pageRS.next()) {
                    Long id1 = pageRS.getLong("id1");
                    Long id2 = pageRS.getLong("id2");

                    int s = pageRS.getInt("status");
                    LocalDateTime localDateTime = pageRS.getTimestamp("friendsFrom").toLocalDateTime();

                    Friendship friendship = new Friendship(new Tuple<>(id1, id2));
                    friendship.setStatus(statusFromInt(s));
                    friendship.setTimeSent(localDateTime);

                    friendships.add(friendship);
                }
                return new Page<>(friendships, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Status statusFromInt(Integer s) {
        return switch (s){
            case 1 -> Status.APPROVED;
            case 2 -> Status.REJECTED;
            default -> Status.PENDING; // case 0 -> Status.PENDING;
        };
    }
}
