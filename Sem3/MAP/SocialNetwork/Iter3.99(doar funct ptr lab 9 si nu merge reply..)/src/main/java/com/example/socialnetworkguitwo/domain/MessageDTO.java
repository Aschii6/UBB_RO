package com.example.socialnetworkguitwo.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDTO extends Entity<Long> {
    private final Long idFrom;
    private final List<Long> to;
    private final String messageText;
    private LocalDateTime timeSent;
    private final Message repliedMessage;

    public MessageDTO(Long idFrom, List<Long> to, String messageText, Message repliedMessage) {
        this.idFrom = idFrom;
        this.to = to;
        this.messageText = messageText;
        this.repliedMessage = repliedMessage;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public List<Long> getTo() {
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
