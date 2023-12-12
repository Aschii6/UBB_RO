package com.example.socialnetworkguitwo.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private final Long idFrom;
    private final List<User> to;
    private final String messageText;
    private LocalDateTime timeSent;
    private final Message repliedMessage;

    public Message(Long idFrom, List<User> to, String messageText, Message repliedMessage) {
        this.idFrom = idFrom;
        this.to = to;
        this.messageText = messageText;
        this.repliedMessage = repliedMessage;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public List<User> getTo() {
        return to;
    }

    public String getMessageText() {
        return messageText;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}
