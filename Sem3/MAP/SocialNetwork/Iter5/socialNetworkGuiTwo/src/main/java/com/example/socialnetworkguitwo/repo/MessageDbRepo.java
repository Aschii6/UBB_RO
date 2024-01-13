package com.example.socialnetworkguitwo.repo;

import com.example.socialnetworkguitwo.domain.Message;
import com.example.socialnetworkguitwo.domain.MessageDTO;
import com.example.socialnetworkguitwo.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDbRepo implements MessageRepository {
    private final String url;
    private final String username;
    private final String password;

    public MessageDbRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        return null;
    }

    @Override
    public Optional<Message> save(Message message) {
        if (message == null)
            throw new IllegalArgumentException("Message must not be null.\n");

        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages " +
                        "(idFrom, messageText, idMessageThisRepliedTo) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);

                PreparedStatement toUsersStatement = connection.prepareStatement("INSERT INTO toUsers " +
                        "(idmessage, idto) VALUES (?, ?)")
        ){
            preparedStatement.setLong(1, message.getIdFrom());
            preparedStatement.setString(2, message.getMessageText());
            if (message.getRepliedMessage() != null)
                preparedStatement.setLong(3, message.getRepliedMessage().getId());
            else
                preparedStatement.setLong(3, 0L);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return Optional.of(message);
            } else {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    message.setId(generatedId);
                } else
                    return Optional.of(message);

                for (User user : message.getTo()) {
                    toUsersStatement.setLong(1, message.getId());
                    toUsersStatement.setLong(2, user.getId());

                    toUsersStatement.executeUpdate();
                }
                return Optional.empty();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<MessageDTO> getAllMessagesBetweenTwoUsers(Long id1, Long id2) {
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE " +
                        "idfrom = ? OR idfrom = ?");

                PreparedStatement toUsersStatement = connection.prepareStatement("SELECT * FROM toUsers WHERE " +
                        "idmessage = ?")
        ) {
            preparedStatement.setLong(1, id1);
            preparedStatement.setLong(2, id2);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<MessageDTO> messageDTOS = new ArrayList<>();
            while (resultSet.next()) {
                Long idMessage = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("idFrom");
                String messageText = resultSet.getString("messageText");
                LocalDateTime localDateTime = resultSet.getTimestamp("timeSent").toLocalDateTime();
                Long idMessageThisRepliedTo = resultSet.getLong("idMessageThisRepliedTo");

                toUsersStatement.setLong(1, idMessage);

                ResultSet toUsersResultSet = toUsersStatement.executeQuery();
                List<Long> toIds = new ArrayList<>();

                while (toUsersResultSet.next()) {
                    Long toId = toUsersResultSet.getLong("idTo");

                    toIds.add(toId);
                }

                Message message = null;
                if (idMessageThisRepliedTo != 0) {
                    Optional<Message> opt = findRepliedMessage(idMessageThisRepliedTo);
                    if (opt.isEmpty())
                        throw new RuntimeException("Ceva nu e bine xdd");
                    else
                        message = opt.get();
                }

                if ((idFrom.equals(id1) && toIds.contains(id2)) || (idFrom.equals(id2) && toIds.contains(id1))) {
                    MessageDTO messageDTO = new MessageDTO(idFrom, toIds, messageText, message);
                    messageDTO.setId(idMessage);
                    messageDTO.setTimeSent(localDateTime);

                    messageDTOS.add(messageDTO);
                }
            }
            return messageDTOS;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DOAR PENTRU REPLIED MESSAGE CA SA AM STRINGUL ()
    private Optional<Message> findRepliedMessage(Long id) {
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE" +
                        " id = ?")
        ) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String messageText = resultSet.getString("messageText");

                List<User> to = new ArrayList<>();
                Message message = new Message(0L, to, messageText, null);
                message.setId(id);
                return Optional.of(message);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
