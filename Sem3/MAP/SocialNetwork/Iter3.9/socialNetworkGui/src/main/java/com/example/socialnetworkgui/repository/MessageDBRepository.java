package com.example.socialnetworkgui.repository;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.Tuple;
import com.example.socialnetworkgui.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE" +
                        " id = ?")
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Long idFrom = resultSet.getLong("idFrom");
                Long idTo = resultSet.getLong("idTo");
                String messageText = resultSet.getString("messageText");
                LocalDateTime timeSent = resultSet.getTimestamp("timeSent").toLocalDateTime();

                Long idMessageThisRepliedTo = resultSet.getLong("idMessageThisRepliedTo");

                Message message = new Message(idFrom, idTo, messageText);
                message.setId(id);
                message.setTimeSent(timeSent);

                if (idMessageThisRepliedTo != null)
                    message.setIdMessageThisRepliedTo(idMessageThisRepliedTo);

                return Optional.of(message);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages")
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");

                Long idFrom = resultSet.getLong("idFrom");
                Long idTo = resultSet.getLong("idTo");
                String messageText = resultSet.getString("messageText");
                LocalDateTime timeSent = resultSet.getTimestamp("timeSent").toLocalDateTime();

                Long idMessageThisRepliedTo = resultSet.getLong("idMessageThisRepliedTo");

                Message message = new Message(idFrom, idTo, messageText);
                message.setId(id);
                message.setTimeSent(timeSent);

                if (idMessageThisRepliedTo != null)
                    message.setIdMessageThisRepliedTo(idMessageThisRepliedTo);

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message message) {
        if (message == null)
            throw new IllegalArgumentException("Message must not be null.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages " +
                        "(idFrom, idTo, messageText, idMessageThisRepliedTo) VALUES (?,?,?,?)")
        ){
            preparedStatement.setLong(1, message.getIdFrom());
            preparedStatement.setLong(2, message.getIdTo());
            preparedStatement.setString(3, message.getMessageText());
            preparedStatement.setLong(4, message.getIdMessageThisRepliedTo());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 0 ? Optional.of(message) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*@Override
    public Optional<Message> save(Message message) { // ptr to list, findOne putin mai complicat, findAll relativ usor
        if (message == null)
            throw new IllegalArgumentException("Message must not be null.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages " +
                        "(idFrom, idTo, messageText, idMessageThisRepliedTo) VALUES (?,?,?,?)")
        ){
            int totalAffectedRows = 0;
            for (User user : message.getTo()) {
                preparedStatement.setLong(1, message.getIdFrom());
                preparedStatement.setLong(2, user.getId());
                preparedStatement.setString(3, message.getMessageText());
                preparedStatement.setLong(4, message.getIdMessageThisRepliedTo());

                int affectedRows = preparedStatement.executeUpdate();
                totalAffectedRows += affectedRows;
            }
            return totalAffectedRows == 0 ? Optional.of(message) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public Optional<Message> delete(Long id) {
        Optional<Message> opt = this.findOne(id);

        if (opt.isPresent()){
            try(
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages " +
                            "WHERE id = ?")
            ){
                preparedStatement.setLong(1, id);

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
    public Optional<Message> update(Message message) {
        if (message == null)
            throw new IllegalArgumentException("Message must not be null.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE messages " +
                        "SET messagetext = ? WHERE (id = ?)")
        ){
            preparedStatement.setString(1, message.getMessageText());
            preparedStatement.setLong(2, message.getId());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows == 0 ? Optional.of(message) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
